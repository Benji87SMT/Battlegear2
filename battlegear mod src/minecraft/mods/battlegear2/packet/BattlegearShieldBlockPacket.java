package mods.battlegear2.packet;


import mods.battlegear2.Battlegear;
import mods.battlegear2.utils.BattlegearUtils;
import mods.battlegear2.utils.EnumBGAnimations;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.WorldServer;

import java.io.*;

public class BattlegearShieldBlockPacket extends AbstractMBPacket{
    public static final String packetName = "MB2|Block";

    public static Packet250CustomPayload generatePacket(boolean block, String username) {

        ByteArrayOutputStream bos = null;
        DataOutputStream outputStream = null;
        try {
            bos = new ByteArrayOutputStream();
            outputStream = new DataOutputStream(bos);

            outputStream.writeBoolean(block);
            Packet.writeString(username, outputStream);

            return new Packet250CustomPayload(packetName, bos.toByteArray());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            BattlegearUtils.closeStream(outputStream);
        }


        return null;
    }

    @Override
    public void process(Packet250CustomPayload packet, EntityPlayer player) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));

        String playername = null;
        boolean block = true;

        try {
            block = inputStream.readBoolean();
            playername = Packet.readString(inputStream, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            BattlegearUtils.closeStream(inputStream);
        }

        if (playername != null) {

            EntityPlayer entity = player.worldObj.getPlayerEntityByName(playername);

            if(entity!=null){
	            if (player.worldObj instanceof WorldServer) {
	                ((WorldServer) player.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(entity, packet);
	            }
	
	            player.setBlockingWithShield(block);
            }
        }
    }
}
