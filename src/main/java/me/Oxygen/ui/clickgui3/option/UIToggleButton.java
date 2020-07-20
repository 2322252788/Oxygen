package me.Oxygen.ui.clickgui3.option;

import me.Oxygen.Oxygen;
import me.Oxygen.ui.clickgui3.ClickMenu;
import me.Oxygen.ui.clickgui3.button.Button;
import me.Oxygen.value.Value;

public class UIToggleButton extends Button{
   public Value value;
   public ClickMenu clickmenu;
   public String name;
   public boolean state;

   public UIToggleButton(ClickMenu clickmenu, String name, boolean state, Value value) {
       super(name, state);
      this.clickmenu = clickmenu;
      this.value = value;
      this.name = name;
      this.state = state;
   }

   public void onPress() {
      if (this.parent != null) {
         if (this.parent.equals(Oxygen.INSTANCE.crink.menu.currentMod.getName())) {
            value.setValueState(!((Boolean)value.getValueState()));
            Oxygen.INSTANCE.FileMgr.saveValues();
            super.onPress();
         }
      }
   }
   
   
}
