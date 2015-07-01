package com.arc.bloodarsenal.common.entity.projectile;

import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;

public class EntityGatlingProjectile extends Entity implements IProjectile, IThrowableEntity
{
    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected int inTile = 0;
    protected int inData = 0;
    protected boolean inGround = false;
    public EntityLivingBase shootingEntity;
    protected int ticksInAir = 0;
    protected int maxTicksInAir = 600;
    private int ricochetCounter = 0;
    private boolean scheduledForDeath = false;
    protected int projectileDamage = 8;

    public EntityGatlingProjectile(World par1World)
    {
        super(par1World);
        setSize(0.5F, 0.5F);
        maxTicksInAir = 600;
    }

    public EntityGatlingProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage)
    {
        super(par1World);
        shootingEntity = par2EntityPlayer;
        float par3 = 0.8F;
        setSize(0.5F, 0.5F);
        setLocationAndAngles(par2EntityPlayer.posX, par2EntityPlayer.posY + par2EntityPlayer.getEyeHeight(), par2EntityPlayer.posZ, par2EntityPlayer.rotationYaw, par2EntityPlayer.rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
        projectileDamage = damage;
        maxTicksInAir = 600;
    }

    public EntityGatlingProjectile(World par1World, EntityLivingBase par2EntityPlayer, int damage, int maxTicksInAir, double posX, double posY, double posZ, float rotationYaw, float rotationPitch)
    {
        super(par1World);
        shootingEntity = par2EntityPlayer;
        float par3 = 0.8F;
        setSize(0.5F, 0.5F);
        setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        posX -= MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        posY -= 0.2D;
        posZ -= MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
        setPosition(posX, posY, posZ);
        yOffset = 0.0F;
        motionX = -MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionZ = MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI);
        motionY = -MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI);
        setThrowableHeading(motionX, motionY, motionZ, par3 * 1.5F, 1.0F);
        projectileDamage = damage;
    }

    public EntityGatlingProjectile(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5, int damage, int maxTicksInAir)
    {
        super(par1World);
        renderDistanceWeight = 10.0D;
        shootingEntity = par2EntityLivingBase;
        posY = par2EntityLivingBase.posY + (double) par2EntityLivingBase.getEyeHeight() - 0.10000000149011612D;
        double d0 = par3EntityLivingBase.posX - par2EntityLivingBase.posX;
        double d1 = par3EntityLivingBase.boundingBox.minY + (double) (par3EntityLivingBase.height / 1.5F) - posY;
        double d2 = par3EntityLivingBase.posZ - par2EntityLivingBase.posZ;
        double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f2 = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            setLocationAndAngles(par2EntityLivingBase.posX + d4, posY, par2EntityLivingBase.posZ + d5, f2, f3);
            yOffset = 0.0F;
            float f4 = (float) d3 * 0.2F;
            setThrowableHeading(d0, d1, d2, par4, par5);
        }

        projectileDamage = damage;
    }
    
    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(16, 0);
    }

    @Override
    public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8)
    {
        float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
        var1 /= var9;
        var3 /= var9;
        var5 /= var9;
        var1 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var3 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var5 += rand.nextGaussian() * 0.007499999832361937D * var8;
        var1 *= var7;
        var3 *= var7;
        var5 *= var7;
        motionX = var1;
        motionY = var3;
        motionZ = var5;
        float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
        prevRotationYaw = rotationYaw = (float) (Math.atan2(var1, var5) * 180.0D / Math.PI);
        prevRotationPitch = rotationPitch = (float) (Math.atan2(var3, var10) * 180.0D / Math.PI);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        setPosition(par1, par3, par5);
        setRotation(par7, par8);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        motionX = par1;
        motionY = par3;
        motionZ = par5;

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(par3, var7) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch;
            prevRotationYaw = rotationYaw;
            setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (ticksInAir > maxTicksInAir)
        {
            setDead();
        }

        if (shootingEntity == null)
        {
            List players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(posX - 1, posY - 1, posZ - 1, posX + 1, posY + 1, posZ + 1));
            Iterator i = players.iterator();
            double closestDistance = Double.MAX_VALUE;
            EntityPlayer closestPlayer = null;

            while (i.hasNext())
            {
                EntityPlayer e = (EntityPlayer) i.next();
                double distance = e.getDistanceToEntity(this);

                if (distance < closestDistance)
                {
                    closestPlayer = e;
                }
            }

            if (closestPlayer != null)
            {
                shootingEntity = closestPlayer;
            }
        }

        if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F)
        {
            float var1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
            prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, var1) * 180.0D / Math.PI);
        }

        Block var16 = worldObj.getBlock(xTile, yTile, zTile);

        if (var16 != null)
        {
            var16.setBlockBoundsBasedOnState(worldObj, xTile, yTile, zTile);
            AxisAlignedBB var2 = var16.getCollisionBoundingBoxFromPool(worldObj, xTile, yTile, zTile);

            if (var2 != null && var2.isVecInside(Vec3.createVectorHelper(posX, posY, posZ)))
            {
                inGround = true;
            }
        }

        if (inGround)
        {
            Block var18 = worldObj.getBlock(xTile, yTile, zTile);
            int var19 = worldObj.getBlockMetadata(xTile, yTile, zTile);

            if (var18.equals(Block.getBlockById(inTile)) && var19 == inData){}
        }
        else
        {
            ++ticksInAir;

            if (ticksInAir > 1 && ticksInAir < 3)
            {
                for (int particles = 0; particles < 3; particles++)
                {
                    doFiringParticles();
                }
            }

            Vec3 var17 = Vec3.createVectorHelper(posX, posY, posZ);
            Vec3 var3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
            MovingObjectPosition var4 = worldObj.func_147447_a(var17, var3, true, false, false);
            var17 = Vec3.createVectorHelper(posX, posY, posZ);
            var3 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

            if (var4 != null)
            {
                var3 = Vec3.createVectorHelper(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
            }

            Entity var5 = null;
            List var6 = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            Iterator var9 = var6.iterator();
            float var11;

            while (var9.hasNext())
            {
                Entity var10 = (Entity) var9.next();

                if (var10.canBeCollidedWith() && (var10 != shootingEntity || ticksInAir >= 5))
                {
                    var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);

                    if (var13 != null)
                    {
                        double var14 = var17.distanceTo(var13.hitVec);

                        if (var14 < var7 || var7 == 0.0D)
                        {
                            var5 = var10;
                            var7 = var14;
                        }
                    }
                }
            }

            if (var5 != null)
            {
                var4 = new MovingObjectPosition(var5);
            }

            if (var4 != null)
            {
                onImpact(var4);

                if (scheduledForDeath)
                {
                    setDead();
                }
            }

            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
            setPosition(posX, posY, posZ);
        }
    }

    public void doFiringParticles()
    {
        worldObj.spawnParticle("mobSpellAmbient", posX + smallGauss(0.1D), posY + smallGauss(0.1D), posZ + smallGauss(0.1D), 0.5D, 0.5D, 0.5D);
        worldObj.spawnParticle("flame", posX, posY, posZ, gaussian(motionX), gaussian(motionY), gaussian(motionZ));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("xTile", (short) xTile);
        par1NBTTagCompound.setShort("yTile", (short) yTile);
        par1NBTTagCompound.setShort("zTile", (short) zTile);
        par1NBTTagCompound.setByte("inTile", (byte) inTile);
        par1NBTTagCompound.setByte("inData", (byte) inData);
        par1NBTTagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
        par1NBTTagCompound.setInteger("ticksInAir", ticksInAir);
        par1NBTTagCompound.setInteger("maxTicksInAir", maxTicksInAir);
        par1NBTTagCompound.setInteger("projectileDamage", projectileDamage);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        xTile = par1NBTTagCompound.getShort("xTile");
        yTile = par1NBTTagCompound.getShort("yTile");
        zTile = par1NBTTagCompound.getShort("zTile");
        inTile = par1NBTTagCompound.getByte("inTile") & 255;
        inData = par1NBTTagCompound.getByte("inData") & 255;
        inGround = par1NBTTagCompound.getByte("inGround") == 1;
        ticksInAir = par1NBTTagCompound.getInteger("ticksInAir");
        maxTicksInAir = par1NBTTagCompound.getInteger("maxTicksInAir");
        projectileDamage = par1NBTTagCompound.getInteger("projectileDamage");
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    @Override
    public boolean canAttackWithItem()
    {
        return false;
    }

    public void onImpact(MovingObjectPosition mop)
    {
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
        {
            if (mop.entityHit == shootingEntity)
            {
                return;
            }

            onImpact(mop.entityHit);
        }
        else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            groundImpact(mop.sideHit);
            worldObj.createExplosion(shootingEntity, posX, posY, posZ, (float) 0.2, true);
        }
    }

    public void onImpact(Entity mop)
    {
        if (mop == shootingEntity && ticksInAir > 3)
        {
            shootingEntity.attackEntityFrom(DamageSource.causeMobDamage(shootingEntity), 1);
            setDead();
        }
        else
        {
            doDamage(projectileDamage + d6(), mop);
            worldObj.createExplosion(shootingEntity, posX, posY, posZ, (float) 0.2, true);
        }

        spawnHitParticles("magicCrit", 8);
        setDead();
    }

    private int d6()
    {
        return rand.nextInt() + 5;
    }

    protected void spawnHitParticles(String string, int i)
    {
        for (int particles = 0; particles < i; particles++)
        {
            worldObj.spawnParticle(string, posX, posY - (string == "portal" ? 1 : 0), posZ, gaussian(motionX), gaussian(motionY), gaussian(motionZ));
        }
    }

    protected void doDamage(int i, Entity mop)
    {
        mop.attackEntityFrom(getDamageSource(), i);
    }

    public DamageSource getDamageSource()
    {
        return DamageSource.causeMobDamage(shootingEntity);
    }

    public void groundImpact(int sideHit)
    {
        ricochet(sideHit);
    }

    public double smallGauss(double d)
    {
        return (worldObj.rand.nextFloat() - 0.5D) * d;
    }

    public double gaussian(double d)
    {
        return d + d * ((rand.nextFloat() - 0.5D) / 4);
    }

    private void ricochet(int sideHit)
    {
        switch (sideHit)
        {
            case 0:
            case 1:
                // topHit, bottomHit, reflect Y
                motionY = motionY * -1;
                break;

            case 2:
            case 3:
                // westHit, eastHit, reflect Z
                motionZ = motionZ * -1;
                break;

            case 4:
            case 5:
                // southHit, northHit, reflect X
                motionX = motionX * -1;
                break;
        }

        ricochetCounter++;

        if (ricochetCounter > getRicochetMax())
        {
            scheduledForDeath = true;

            for (int particles = 0; particles < 4; particles++)
            {
                switch (sideHit)
                {
                    case 0:
                        worldObj.spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), -gaussian(0.1D), gaussian(0.1D));
                        break;

                    case 1:
                        worldObj.spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                        break;

                    case 2:
                        worldObj.spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), -gaussian(0.1D));
                        break;

                    case 3:
                        worldObj.spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                        break;

                    case 4:
                        worldObj.spawnParticle("smoke", posX, posY, posZ, -gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                        break;

                    case 5:
                        worldObj.spawnParticle("smoke", posX, posY, posZ, gaussian(0.1D), gaussian(0.1D), gaussian(0.1D));
                        break;
                }
            }
        }
    }

    private int getRicochetMax()
    {
        return 3;
    }

    @Override
    public Entity getThrower()
    {
        return shootingEntity;
    }

    @Override
    public void setThrower(Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            shootingEntity = (EntityLivingBase) entity;
        }
    }
}