package monad

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.Random

trait AdventureAsync {
    def collectionCoinsWithFallBack(): Future[List[Coin]]

    def buyTreasure(coins: List[Coin]): Future[Treasure]
}

class AdventureAsyncImpl extends AdventureAsync {

    override def collectionCoinsWithFallBack(): Future[List[Coin]] = collectionCoins().recoverWith(e => collectionCoinsWithFallBack())

    private def collectionCoins(): Future[List[Coin]] = {
        Future {
            // do some network needed things or serialization things
            if (eatenByMonster()) throw GameOverException("Oops!")
            var coins: List[Coin] = Nil
            1 to 2 foreach (_ => coins = coins :+ (if (Random.nextBoolean()) Silver() else Gold()))
            coins
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

    1 to 10 foreach (_ => {
        val treasure: Treasure = Await.result(findTreasureByAdventure(adventure), 3.seconds) // todo resilient, recovery policy
        println(treasure)
    })

    def findTreasureByAdventure(adventure: AdventureAsync): Future[Treasure] = adventure.collectionCoinsWithFallBack()
        .flatMap(coins => adventure.buyTreasure(coins).recoverWith(e => findTreasureByAdventure(adventure)))

    def findTreasureByAdventureWithPromise(adventure: AdventureAsync): Future[Treasure] = {
        val eventualTreasure: Future[Treasure] = adventure.collectionCoinsWithFallBack()
            .flatMap(coins => adventure.buyTreasure(coins).recoverWith(e => findTreasureByAdventure(adventure)))

        val promisedTreasure: Promise[Treasure] = Promise[Treasure]()
        promisedTreasure.completeWith(eventualTreasure)
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