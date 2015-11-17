# camunda-bpm-reactor

Event-based listeners and delegates for camunda.

## What is this about? 

This extension provides a process engine plugin that registers Execution- and TaskListeners to all possible elements and all possible events. These listeners then publish their delegates (DelegateTask, DelegateExcution) to an event bus.

Custom implementations that are interested in certain events may register on that bus and will get notified and executed when the registered listeners fire.

Publishing and subscribing uses a topic pattern `/camunda/{type}/{process}/{element}/{event}` so it is possible to register on all that happens on the engine or the assignement event of a concrete userTask in a concrete process.

Reference: a similar approach was already done in the engine-cdi module using CDI observers and qualifiers. 

## Why do I need this?

Using an event bus decouples registration and implementation of listeners. The bpmn file has not to be touched for this. This is useful for implementations that can be considered "aspects" of the engine like task assignment and monitoring.

Instead of registering listeners all over your bpmn files that always call the same rule service to determine the candidate groups of a task or write runtime information to a data source, you just hook into the event bus stream and wait for notification.

While this could be achieved with custom plugins/parselisteners, these share the problem that the engine has to have access to the code of the listeners added. With the decoupling via eventbus, this is avoided.

## How is it done?

This extension uses the event bus provided by projectreactor.io. This bus is fairly advanced and stable and allows separation of event-payload (the DelegateExpression) and event topic, so we do not need any additional qualifiers or concrete types to distinct between "listen to create of task B" or "listen to all events of task A".

Using the extension is straight forward, you need to:

* access the eventBus instance used in the plugin by calling CamundaReactor.eventBus().
* Use this bus to register an instance of an appropriate listener and specify the topic parts you are interested in.

### Examples

Register a listener that is fired for all "create" events on any user task in the system. 

```java
@CamundaSelector(type = "userTask", event = TaskListener.EVENTNAME_CREATE)
public class TaskCreateListener extends SubscriberTaskListener {
  
  public TaskCreateListener(EventBus eventBus) {
    register(eventBus);
  }

  @Override
  public void notify(DelegateTask delegateTask) {
   ...
  }
}
```

## Noteworthy

This extensions works with delegateTasks and delegateEvents directly. These cannot be used outside the current thread, so the eventBus used is synchronous. 

## Net Steps

* publishing as an official communit extension on camunda
* releasing 1.0 to maven central
* provide extensions for spring and CDI
* ...

## Maintainer

* [Jan Galinski](https://github.com/jangalinski), [Holisticon AG](http://www.holisticon.de/)

## License

* [Apache License, Version 2.0](./LICENSE)
