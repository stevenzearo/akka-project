package frp

import frp.Signal.caller

import scala.util.DynamicVariable

class Signal[T](expr: => T) {
    private var myExpr: () => T = _
    private var myValue: T = _
    private var observers: Set[Signal[_]]= Set()
    update(expr)

    def update(expr: => T): Unit = {
        myExpr = () => expr
        computeValue()
    }

    protected def computeValue(): Unit = {
        val newValue = caller.withValue(this)(myExpr())
        if (myValue != newValue) {
            myValue = newValue
            val obs = observers
            observers = Set()
            obs.foreach(_.computeValue()) // all signal's value will be updated
        }
    }

    def apply(): T = {
        observers += caller.value
        assert(!caller.value.observers.contains(this), "cycling signal define")
        myValue
    }
}

object NoSignal extends Signal[Unit](()) {
    override def computeValue(): Unit = ()
}
object Signal {
    private val caller = new DynamicVariable[Signal[_]](NoSignal)
    def apply[T](expr: => T): Signal[T] = new Signal(expr)
}
