package the_fireplace.mechsoldiers.items;

import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import the_fireplace.mechsoldiers.entity.EntityMechSkeleton;
import the_fireplace.mechsoldiers.util.IBrain;
import the_fireplace.overlord.Overlord;
import the_fireplace.overlord.entity.EntityArmyMember;
import the_fireplace.overlord.entity.ai.*;

/**
 * @author The_Fireplace
 */
public class ItemBrain extends Item implements IBrain {

    private String material;

    public ItemBrain(String material, int durability){
        setUnlocalizedName("brain_"+material);
        setRegistryName("brain_"+material);
        setMaxDamage(durability);
        this.material=material;
        setCreativeTab(Overlord.tabOverlord);
    }
    protected int raiseArmTicks;
    protected EntityAIAttackMelee aiAttackOnCollide = null;

    @Override
    public void addAttackAi(EntityMechSkeleton skeleton, byte mode) {
        if(aiAttackOnCollide == null){
            aiAttackOnCollide = new EntityAIAttackMelee(skeleton, 1.2D, false)
            {
                @Override
                public void resetTask()
                {
                    super.resetTask();
                    skeleton.setSwingingArms(false);
                }

                @Override
                public void startExecuting()
                {
                    super.startExecuting();
                    raiseArmTicks = 0;
                }

                @Override
                public void updateTask(){
                    if(continueExecuting()){
                        ++raiseArmTicks;

                        if (raiseArmTicks >= 5 && this.attackTick < 10)
                        {
                            skeleton.setSwingingArms(true);
                        }
                        else
                        {
                            skeleton.setSwingingArms(false);
                        }
                        super.updateTask();
                    }
                }
            };
        }
        if(skeleton.getMovementMode() > 0)
            skeleton.tasks.addTask(5, aiAttackOnCollide);
    }

    @Override
    public void addTargetAi(EntityMechSkeleton skeleton, byte mode) {
        switch(mode) {
            case 0:
            default:
                skeleton.setAttackTarget(null);
                skeleton.setRevengeTarget(null);
                break;
            case 2:
                skeleton.targetTasks.addTask(2, new EntityAIMasterHurtTarget(skeleton));
            case 1:
                skeleton.targetTasks.addTask(1, new EntityAIMasterHurtByTarget(skeleton));
                skeleton.targetTasks.addTask(1, new EntityAIHurtByNonAllied(skeleton, !material.equals("copper_redstone"), new Class[0]));
                skeleton.targetTasks.addTask(2, new EntityAINearestNonTeamTarget(skeleton, EntityPlayer.class, true));
                skeleton.targetTasks.addTask(2, new EntityAINearestNonTeamTarget(skeleton, EntityArmyMember.class, true));
                skeleton.targetTasks.addTask(3, new EntityAINearestNonTeamTarget(skeleton, IMob.class, true));
        }
    }

    @Override
    public void addMovementAi(EntityMechSkeleton skeleton, byte mode) {
        switch(mode) {
            case 1:
                if(skeleton.shouldMobAttack(new EntityCreeper(skeleton.world))) {
                    skeleton.tasks.addTask(3, new EntityAIAvoidEntity(skeleton, EntityCreeper.class, 10.0F, 1.2D, 1.6D));
                }
                skeleton.tasks.addTask(4, new EntityAIOpenDoor(skeleton, material.contains("redstone")));
                skeleton.tasks.addTask(6, new EntityAIFollowMaster(skeleton, 1.0D, 10.0F, 2.0F));
            case 0:
                skeleton.setHomePosAndDistance(new BlockPos(skeleton.posX, skeleton.posY, skeleton.posZ), -1);
                break;
            case 2:
            default:
                skeleton.setHomePosAndDistance(new BlockPos(skeleton.posX, skeleton.posY, skeleton.posZ), 20);
                if(skeleton.shouldMobAttack(new EntityCreeper(skeleton.world))) {
                    skeleton.tasks.addTask(3, new EntityAIAvoidEntity(skeleton, EntityCreeper.class, 10.0F, 1.2D, 1.6D));
                }
                skeleton.tasks.addTask(4, new EntityAIOpenDoor(skeleton, material.contains("redstone")));
                skeleton.tasks.addTask(7, new EntityAIWanderBase(skeleton, 1.0D));
        }
    }
}