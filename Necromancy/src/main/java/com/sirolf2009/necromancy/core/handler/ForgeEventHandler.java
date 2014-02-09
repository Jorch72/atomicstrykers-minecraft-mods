package com.sirolf2009.necromancy.core.handler;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;

import com.sirolf2009.necromancy.Necromancy;
import com.sirolf2009.necromancy.achievement.AchievementNecromancy;
import com.sirolf2009.necromancy.block.BlockNecromancy;
import com.sirolf2009.necromancy.client.model.ModelMinion;
import com.sirolf2009.necromancy.command.CommandMinion;
import com.sirolf2009.necromancy.command.CommandRemodel;
import com.sirolf2009.necromancy.item.ItemGeneric;
import com.sirolf2009.necromancy.item.ItemNecroSkull;
import com.sirolf2009.necromancy.item.ItemNecromancy;

import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ForgeEventHandler
{

    @SubscribeEvent
    public void LivingDropsEvent(LivingDeathEvent evt)
    {
        if (evt.entity instanceof EntityLiving && !evt.entity.worldObj.isRemote && rand.nextInt(100) <= 8)
        {
            switch (rand.nextInt(7))
            {
            case 0:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 0), 1);
                break; // brains
            case 1:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 1), 1);
                break; // heart
            case 2:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 2), 1);
                break; // muscle
            case 3:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 2), 1);
                break; // muscle
            case 4:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 2), 1);
                break; // muscle
            case 5:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 2), 1);
                break; // muscle
            case 6:
                evt.entity.entityDropItem(new ItemStack(ItemNecromancy.organs, 1, 3), 1);
                break; // lungs
            }
        }
    }

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event)
    {
        ItemStack item = event.crafting;
        EntityPlayer player = event.player;
        IInventory craftMatrix = event.craftMatrix;
        if (item != null && item.getUnlocalizedName().equals("Items.NecronomIIcon"))
        {
            player.addStat(AchievementNecromancy.NecronomIIconAchieve, 1);
        }
        if (item != null && item.getUnlocalizedName().equals("tile.Sewing Machine"))
        {
            player.addStat(AchievementNecromancy.SewingAchieve, 1);
        }
        if (item != null && item == new ItemStack(ItemNecromancy.bucketBlood))
        {
            player.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle, 8));
        }
        if (item != null && item == ItemGeneric.getItemStackFromName("Jar of Blood")
                && item.getItemDamage() == ItemGeneric.getItemStackFromName("Jar of Blood").getItemDamage())
        {
            player.inventory.addItemStackToInventory(new ItemStack(Items.bucket));
        }
        if (item != null && item.getUnlocalizedName().equals("tile.skullWall"))
        {
            Necromancy.loggerNecromancy.info(craftMatrix.getStackInSlot(0) + " is in " + craftMatrix.getStackInSlot(0).getUnlocalizedName());
            item.stackTagCompound.setString("Base", craftMatrix.getStackInSlot(1).getUnlocalizedName());
            item.stackTagCompound.setString("Skull1", ItemNecroSkull.skullTypes[craftMatrix.getStackInSlot(1).getItemDamage()]);
            item.stackTagCompound.setString("Skull2", ItemNecroSkull.skullTypes[craftMatrix.getStackInSlot(4).getItemDamage()]);
            item.stackTagCompound.setString("Skull3", ItemNecroSkull.skullTypes[craftMatrix.getStackInSlot(5).getItemDamage()]);

        }
    }

    public void initCommands(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandRemodel());
        event.registerServerCommand(new CommandMinion());
    }

    @SubscribeEvent
    public void CommandEvent(net.minecraftforge.event.CommandEvent evt)
    {
        if (evt.command instanceof CommandRemodel)
        {
            ModelMinion.remodelCommand = true;
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {

        ItemStack result = fillCustomBucket(event.world, event.target);

        if (result == null)
            return;

        event.result = result;
        event.setResult(Result.ALLOW);
    }

    public ItemStack fillCustomBucket(World world, MovingObjectPosition pos)
    {

        Block blockID = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);

        if ((blockID == BlockNecromancy.fluidBlood.getBlock() || blockID == BlockNecromancy.blood)
                && world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ) == 0)
        {

            world.setBlock(pos.blockX, pos.blockY, pos.blockZ, Blocks.air, 0, 0);

            return new ItemStack(ItemNecromancy.bucketBlood);
        }
        else
            return null;
    }

    Random rand = new Random();
}