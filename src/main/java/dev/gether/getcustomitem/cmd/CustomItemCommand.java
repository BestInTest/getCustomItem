package dev.gether.getcustomitem.cmd;

import dev.gether.getconfig.utils.MessageUtil;
import dev.gether.getcustomitem.GetCustomItem;
import dev.gether.getcustomitem.item.CustomItem;
import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.context.Context;
import dev.rollczi.litecommands.annotations.execute.Execute;
import dev.rollczi.litecommands.annotations.permission.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Command(name = "getcustomitem", aliases = {"getitem", "citem"})
@Permission("getcustomitem.admin")
public class CustomItemCommand {

    private final GetCustomItem getCustomItem;

    public CustomItemCommand(GetCustomItem getCustomItem) {
        this.getCustomItem = getCustomItem;
    }

    /**
     *
     * @param commandSender
     * @param target
     * @param customItem
     * @param amount
     *
     * give target(player) a custom item with specified amount
     */
    @Execute(name = "give")
    public void giveItem(@Context CommandSender commandSender, @Arg("nickname") Player target, @Arg("name_item") CustomItem customItem, @Arg("amount") int amount) {

        ItemStack itemStack = customItem.getItemStack().clone(); // clone item to change amount
        itemStack.setAmount(amount); // set new amount

        target.getInventory().addItem(itemStack); // add item to the player (target)
        MessageUtil.sendMessage(commandSender, "&aSuccessful give the item to player!");

    }

    /**
     *
     * @param commandSender
     *
     * give selector item to the player who using the command
     */
    @Execute(name = "selector")
    public void getSelector(@Context CommandSender commandSender) {
        // check commandSender is player
        if(commandSender instanceof Player player) {
            ItemStack selectorItem = getCustomItem.getSelectorAddon().getSelectorItem(); // get item
            player.getInventory().addItem(selectorItem); // give item to inv
            MessageUtil.sendMessage(player, "&aSuccessful received the selector item!");
        } else {
            MessageUtil.sendMessage(commandSender, "&cYou cannot use this command by console");
        }
    }

    @Execute(name = "debug")
    public void debugItem(@Context CommandSender commandSender) {
        // check commandSender is player
        if(commandSender instanceof Player player) {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            MessageUtil.sendMessage(player, mainHand.toString());
        } else {
            MessageUtil.sendMessage(commandSender, "&cYou cannot use this command by console");
        }
    }




}
