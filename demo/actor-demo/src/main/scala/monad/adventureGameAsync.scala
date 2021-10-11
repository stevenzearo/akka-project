package monad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success}

trait AdventureAsync {
    def collectionCoins(): Future[List[Coin]]

    def buyTreasure(coins: List[Coin]): Future[Treasure]
}

class AdventureAsyncImpl extends AdventureAsync {
    override def collectionCoins(): Future[List[Coin]] = {
        Future {
            // do some network needed things or serialization things
            if (eatenByMonster()) throw GameOverException("Oops!")
            List(Silver(), Silver(), Gold())
        }
    }

    override def buyTreasure(coins: List[Coin]): Future[Treasure] = {
        Future {
            // do some network needed things or serialization things
            val damon = Diamond()
            if (coins.map(_.value).sum < damon.cost) throw GameOverException("Nice try!")
            damon
        }
    }

    private def eatenByMonster(): Boolean = Random.nextBoolean()

}

object AsyncMain extends App {
    val adventure: AdventureAsync = new AdventureAsyncImpl()

    val treasure: Treasure = Await.result(findTreasureByAdventure(adventure), 3.seconds) // todo resilient, recovery policy

    def findTreasureByAdventure(adventure: AdventureAsync): Future[Treasure] = {
        val promisedTreasure: Promise[Treasure] = Promise[Treasure]()
        adventure.collectionCoins().onComplete {
            case Success(x) => adventure.buyTreasure(x).onComplete {
                case Success(y) => promisedTreasure.success(y)
                case Failure(exception) => promisedTreasure.failure(exception)
            }
            case Failure(exception) => promisedTreasure.failure(exception)
        }

        promisedTreasure.future
    }

}

/*
* Async Programing
* Future
* Error handle
* recover()
* recoverWith()
* fallback()
* retry()
* Promise
* Await only used in debugging, should not use in production
* */