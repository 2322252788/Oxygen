package me.Oxygen.event.events;

import me.Oxygen.event.Event;

public class EventKey extends Event {
  public int key;
  
 public EventKey(int key) {
	 this.key = key; 
 }

 
  public int getKey() {
	  return this.key; 
  }


  
  public void setKey(int key) {
	  this.key = key; 
  }
}
