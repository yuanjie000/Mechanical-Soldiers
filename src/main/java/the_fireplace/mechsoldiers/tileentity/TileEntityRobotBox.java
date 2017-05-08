package the_fireplace.mechsoldiers.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import the_fireplace.mechsoldiers.entity.EntityMechSkeleton;

import java.util.Random;
import java.util.UUID;

/**
 * @author The_Fireplace
 */
public class TileEntityRobotBox extends TileEntity implements ITickable {

    private NBTTagCompound skeletonData;
    int ticksRemaining;
    Random rand;
    public TileEntityRobotBox(){
        //Only for use when world is loading.
    }

    public TileEntityRobotBox(NBTTagCompound skeletonData, int ticksToWait){
        this.skeletonData=skeletonData;
        ticksRemaining=ticksToWait;
        rand = new Random();
    }

    public void setSkeletonData(NBTTagCompound nbt){
        skeletonData = nbt;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, getBlockMetadata(), getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag(){
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation("tile.robot_constructor.name");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("ConstructingSkeletonData", skeletonData);
        compound.setInteger("TicksRemaining", ticksRemaining);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        skeletonData = compound.getCompoundTag("ConstructingSkeletonData");
        ticksRemaining = compound.getInteger("TicksRemaining");
    }

    @Override
    public void update() {
        ticksRemaining--;
        if(ticksRemaining <= 0)
            spawnRobot();
    }

    private void spawnRobot(){
        EntityMechSkeleton robot = new EntityMechSkeleton(world, UUID.fromString(skeletonData.getString("OwnerUUID")))
                .setBrain(ItemStack.loadItemStackFromNBT(skeletonData.getCompoundTag("RobotBrain")))
                .setSkeleton(ItemStack.loadItemStackFromNBT(skeletonData.getCompoundTag("RobotSkeleton")))
                .setJoints(ItemStack.loadItemStackFromNBT(skeletonData.getCompoundTag("RobotJoints")));
        robot.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat(), rand.nextFloat());
        world.spawnEntity(robot);
        robot.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), rand.nextFloat(), rand.nextFloat());

        world.destroyBlock(pos, false);
    }
}

