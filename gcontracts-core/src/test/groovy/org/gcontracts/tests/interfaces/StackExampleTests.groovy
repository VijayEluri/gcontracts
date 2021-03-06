package org.gcontracts.tests.interfaces

import org.gcontracts.tests.basic.BaseTestClass
import org.junit.Test

import static org.junit.Assert.*;

/**
 * @author ast
 */
class StackExampleTests extends BaseTestClass {

  def source_stackable = '''
@Contracted
package tests

import org.gcontracts.annotations.*

interface Stackable {

  @Requires({ item != null })
  void push(def item)

  @Ensures({ result != null && old != null })
  def isEmpty()
}
'''

  def source_stack = '''
@Contracted
package tests

import org.gcontracts.annotations.*

class Stack implements Stackable  {

  protected def list

  public Stack()  {
    this.list = []
  }

  public Stack(def list)  {
    this.list = list
  }

  @Ensures({ list.last() == item })
  void push(def item)  {
    list.add item
  }

  def isEmpty()  {
    return list.size() == 0
  }
}
'''

  @Test void creation()  {
    add_class_to_classpath(source_stackable)
    create_instance_of(source_stack)
  }

  @Test void push_precondition()  {
    add_class_to_classpath(source_stackable)
    def stack = create_instance_of(source_stack)

    stack.push 1
    shouldFail AssertionError, {
        stack.push null
    }
  }

  @Test void old_variable_in_postcondition()  {
    add_class_to_classpath(source_stackable)
    def stack = create_instance_of(source_stack)

    assertTrue(stack.isEmpty())
  }

}
