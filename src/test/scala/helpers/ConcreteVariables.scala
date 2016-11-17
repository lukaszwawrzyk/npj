package helpers

import interpreter.MapVariables

trait ConcreteVariables {
  val variables = new MapVariables

  def variable(key: Symbol) = variables.get(key.name)
}