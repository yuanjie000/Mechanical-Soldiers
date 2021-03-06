package the_fireplace.mechsoldiers.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import the_fireplace.mechsoldiers.entity.EntityMechSkeleton;
import the_fireplace.mechsoldiers.util.StainedItemUtil;
import the_fireplace.overlord.entity.EntityArmyMember;

import java.awt.*;

/**
 * @author The_Fireplace
 */
@SideOnly(Side.CLIENT)
public class ModelMechSkeleton extends ModelBiped {

	public ModelRenderer neck;
	public ModelRenderer rightShoulder;
	public ModelRenderer leftShoulder;
	public ModelRenderer rightElbow;
	public ModelRenderer leftElbow;
	public ModelRenderer rightKnee;
	public ModelRenderer leftKnee;
	public boolean isJointLayer;

	public ModelMechSkeleton() {
		this(0.0F, false);
	}

	public ModelMechSkeleton(float modelSize, boolean isJointLayer) {
		super(modelSize, 0.0F, isJointLayer ? 24 : 64, isJointLayer ? 12 : 32);

		this.isJointLayer = isJointLayer;

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4.0F, -9.0F, -4.0F, 8, 8, 8, modelSize);
		this.bipedHead.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.bipedHeadwear = new ModelRenderer(this, 32, 0);
		this.bipedHeadwear.addBox(-4.0F, -9.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
		this.bipedHeadwear.setRotationPoint(0.0F, 0.0F, 0.0F);

		this.bipedRightArm = new ModelRenderer(this, 40, 16);
		this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
		this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		this.bipedLeftArm = new ModelRenderer(this, 40, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
		this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		this.bipedRightLeg = new ModelRenderer(this, 0, 16);
		this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
		this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
		this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
		this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
		if (isJointLayer) {
			this.neck = new ModelRenderer(this, 0, 0);
			this.neck.addBox(-3.0F, -1.0F, -3.0F, 6, 1, 6, modelSize);
			this.leftElbow = new ModelRenderer(this, 8, 8);
			this.leftElbow.addBox(-1.1F, 3.0F, -1.0F, 2, 2, 2, modelSize * 1.1F);
			this.leftElbow.setRotationPoint(5.0F, 2.0F, 0.0F);
			this.rightElbow = new ModelRenderer(this, 8, 8);
			this.rightElbow.addBox(-1.1F, 3.0F, -1.0F, 2, 2, 2, modelSize * 1.1F);
			this.rightElbow.setRotationPoint(-5.0F, 2.0F, 0.0F);
			this.rightElbow.mirror = true;
			this.leftKnee = new ModelRenderer(this, 0, 8);
			this.leftKnee.addBox(-1.0F, 5.0F, -1.0F, 2, 2, 2, modelSize * 1.1F);
			this.leftKnee.setRotationPoint(2.0F, 12.0F, 0.0F);
			this.rightKnee = new ModelRenderer(this, 0, 8);
			this.rightKnee.addBox(-1.0F, 5.0F, -1.0F, 2, 2, 2, modelSize * 1.1F);
			this.rightKnee.setRotationPoint(-2.0F, 12.0F, 0.0F);
			this.rightKnee.mirror = true;
			this.leftShoulder = new ModelRenderer(this, 16, 7);
			this.leftShoulder.addBox(3.5F, -0.1F, -1.5F, 1, 2, 3, modelSize);
			this.rightShoulder = new ModelRenderer(this, 16, 7);
			this.rightShoulder.addBox(-4.5F, -0.1F, -1.5F, 1, 2, 3, modelSize);
			this.rightShoulder.mirror = true;
		}
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		ItemStack itemstack = entitylivingbaseIn.getHeldItem(EnumHand.MAIN_HAND);

		if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW && ((EntityArmyMember) entitylivingbaseIn).isSwingingArms()) {
			if (entitylivingbaseIn.getPrimaryHand() == EnumHandSide.RIGHT) {
				this.rightArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			} else {
				this.leftArmPose = ModelBiped.ArmPose.BOW_AND_ARROW;
			}
		}

		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!isJointLayer) {
			EntityMechSkeleton entityskeleton = (EntityMechSkeleton) entityIn;
			Color color = StainedItemUtil.getColor(entityskeleton.getSkeleton());
			if (color != null)
				GlStateManager.color(((float) color.getRed()) / 255f / 2f, ((float) color.getGreen()) / 255f / 2f, ((float) color.getBlue()) / 255f / 2f);
			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			if (color != null)
				GlStateManager.resetColor();
		} else {
			EntityMechSkeleton entityskeleton = (EntityMechSkeleton) entityIn;
			Color color = StainedItemUtil.getColor(entityskeleton.getJoints());
			if (color != null)
				GlStateManager.color(((float) color.getRed()) / 255f / 2f, ((float) color.getGreen()) / 255f / 2f, ((float) color.getBlue()) / 255f / 2f);
			this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
			GlStateManager.pushMatrix();
			if (entityIn.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}
			this.neck.render(scale);
			this.rightShoulder.render(scale);
			this.leftShoulder.render(scale);
			this.rightElbow.render(scale);
			this.leftElbow.render(scale);
			this.rightKnee.render(scale);
			this.leftKnee.render(scale);

			GlStateManager.popMatrix();
			if (color != null)
				GlStateManager.resetColor();
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		ItemStack itemstack = ((EntityLivingBase) entityIn).getHeldItemMainhand();
		EntityMechSkeleton entityskeleton = (EntityMechSkeleton) entityIn;

		if (entityskeleton.isSwingingArms() && (itemstack.isEmpty() || itemstack.getItem() != Items.BOW)) {
			float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
			float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
			this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F);
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F);
			this.bipedRightArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
			this.bipedLeftArm.rotateAngleX -= f * 1.2F - f1 * 0.4F;
			this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		}
		if (isJointLayer) {
			copyModelAngles(this.bipedLeftLeg, this.leftKnee);
			copyModelAngles(this.bipedRightLeg, this.rightKnee);
			copyModelAngles(this.bipedLeftArm, this.leftElbow);
			copyModelAngles(this.bipedRightArm, this.rightElbow);
			copyModelAngles(this.bipedBody, this.rightShoulder);
			copyModelAngles(this.bipedBody, this.leftShoulder);
			copyModelAngles(this.bipedHead, this.neck);
		}
	}

	@Override
	public void postRenderArm(float scale, EnumHandSide side) {
		float f = side == EnumHandSide.RIGHT ? 1.0F : -1.0F;
		ModelRenderer modelrenderer = this.getArmForSide(side);
		modelrenderer.rotationPointX += f;
		modelrenderer.postRender(scale);
		modelrenderer.rotationPointX -= f;
		if (isJointLayer) {
			ModelRenderer elbow = side == EnumHandSide.RIGHT ? rightElbow : leftElbow;
			elbow.rotationPointX += f;
			elbow.postRender(scale);
			elbow.rotationPointX -= f;
		}
	}
}
