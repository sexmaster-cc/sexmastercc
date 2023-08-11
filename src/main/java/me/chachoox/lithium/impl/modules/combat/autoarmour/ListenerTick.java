package me.chachoox.lithium.impl.modules.combat.autoarmour;

import me.chachoox.lithium.api.property.Property;
import me.chachoox.lithium.api.util.entity.DamageUtil;
import me.chachoox.lithium.api.util.inventory.InventoryUtil;
import me.chachoox.lithium.api.util.thread.MutableWrapper;
import me.chachoox.lithium.impl.event.events.update.TickEvent;
import me.chachoox.lithium.impl.event.listener.ModuleListener;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.DamageStack;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.LevelStack;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.MendingStage;
import me.chachoox.lithium.impl.modules.combat.autoarmour.util.SingleMendingSlot;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.*;

@SuppressWarnings("unchecked")
public class ListenerTick extends ModuleListener<AutoArmour, TickEvent> {
    public ListenerTick(AutoArmour module) {
        super(module, TickEvent.class);
    }

    @Override
    public void call(TickEvent event) {
        if (!event.isSafe()) {
            module.stage = MendingStage.MENDING;
            module.putBackClick = null;
            return;
        }

        module.stackSet = false;
        module.queuedSlots.clear();
        module.windowClicks.clear();

        if (module.pauseInInv.getValue() && mc.currentScreen instanceof GuiInventory) {
            return;
        }

        if (InventoryUtil.validScreen()) {
            if (module.canAutoMend()) {
                module.queuedSlots.add(-2);
                ItemStack setStack = module.setStack();
                boolean setStackIsNull = setStack == null;
                boolean singleMend = module.singleMend.getValue();
                if (setStack == null) {
                    if (!singleMend) {
                        return;
                    }

                    setStack = mc.player.inventory.getItemStack();
                    module.queuedSlots.remove(-2);
                }

                int mendBlock = 25;
                if (module.stage != MendingStage.MENDING) {
                    if (setStackIsNull || isFull()) {
                        module.stage = MendingStage.MENDING;
                        return;
                    }

                    if (module.stage == MendingStage.BLOCK) {
                        if (module.mendingTimer.passed(mendBlock)) {
                            module.stage = MendingStage.TAKEOFF;
                            module.mendingTimer.reset();
                        } else {
                            return;
                        }
                    }

                    if (module.stage == MendingStage.TAKEOFF && module.mendingTimer.passed(50)) {
                        module.stage = MendingStage.MENDING;
                    }
                }

                if (singleMend) {
                    doSingleMend(setStack, mendBlock);
                } else {
                    doNormalMend(setStack, mendBlock);
                }
            } else {
                module.stage = MendingStage.MENDING;
                module.unblockMendingSlots();
                Map<EntityEquipmentSlot, Integer> map = setup(!module.strict.getValue());
                int last = -1;
                ItemStack drag = mc.player.inventory.getItemStack();
                for (Map.Entry<EntityEquipmentSlot, Integer> entry : map.entrySet()) {
                    if (entry.getValue() == 8) {
                        int slot = AutoArmour.fromEquipment(entry.getKey());
                        if (slot != -1 && slot != 45) {
                            ItemStack inSlot = InventoryUtil.get(slot);
                            module.queueClick(slot, inSlot, drag);
                            drag = inSlot;
                            last = slot;
                        }

                        map.remove(entry.getKey());
                        break;
                    }
                }

                for (Map.Entry<EntityEquipmentSlot, Integer> entry : map.entrySet()) {
                    int slot = AutoArmour.fromEquipment(entry.getKey());
                    if (slot != -1 && slot != 45 && entry.getValue() != null) {
                        int i = entry.getValue();
                        ItemStack inSlot = InventoryUtil.get(i);
                        module.queueClick(i, inSlot, drag).setDoubleClick(module.doubleClick.getValue());

                        if (!drag.isEmpty()) {
                            module.queuedSlots.add(i);
                        }

                        drag = inSlot;
                        inSlot = InventoryUtil.get(slot);
                        module.queueClick(slot, inSlot, drag);
                        drag = inSlot;
                        last = slot;
                    }
                }
                if (module.putBack.getValue()) {
                    if (last != -1) {
                        ItemStack stack = InventoryUtil.get(last);
                        if (!stack.isEmpty()) {
                            module.queuedSlots.add(-2);
                            int air = AutoArmour.findItem(Items.AIR, !module.strict.getValue(), module.queuedSlots);
                            if (air != -1) {
                                ItemStack inSlot = InventoryUtil.get(air);
                                module.putBackClick = module.queueClick(air, inSlot, drag);

                                module.putBackClick.addPost(() -> module.putBackClick = null);
                            }
                        }
                    } else if (module.putBackClick != null && module.putBackClick.isValid()) {
                        module.queueClick(module.putBackClick);
                    } else {
                        module.putBackClick = null;
                    }
                }
            }
        } else {
            module.stage = MendingStage.MENDING;
        }

        module.runClick();
    }

    private boolean checkMendingStage(int mendBlock) {
        if (mendBlock > 0 && module.stage == MendingStage.MENDING) {
            module.stage = MendingStage.BLOCK;
            module.mendingTimer.reset();
            return true;
        }

        return false;
    }

    private boolean isFull() {
        boolean added = false;
        if (!module.drag.getValue()) {
            added = module.queuedSlots.add(-2);
        }

        boolean result = AutoArmour.findItem(Items.AIR, !module.strict.getValue(), module.queuedSlots) == -1;
        if (added) {
            module.queuedSlots.remove(-2);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private void doNormalMend(ItemStack dragIn, int mendBlock) {
        List<DamageStack> stacks = new ArrayList<>(4);
        for (int i = 5; i < 9; i++) {
            ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) != 0) {
                float percent = DamageUtil.getPercent(stack);
                if (percent > ((Property<Integer>) module.damages[i - 5]).getValue()) {
                    stacks.add(new DamageStack(stack, percent, i));
                }
            }
        }

        stacks.sort(DamageStack::compareTo);
        MutableWrapper<ItemStack> drag = new MutableWrapper<>(dragIn);
        for (DamageStack stack : stacks) {
            if (checkDamageStack(stack, mendBlock, drag)) {
                return;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void doSingleMend(ItemStack dragIn, int mendBlock) {
        boolean allBlocked = true;
        for (SingleMendingSlot singleMendingSlot : module.singleMendingSlots) {
            allBlocked = allBlocked && singleMendingSlot.isBlocked();
        }

        if (allBlocked) {
            module.unblockMendingSlots();
        }

        List<DamageStack> stacks = new ArrayList<>(4);
        for (int i = 5; i < 9; i++) {
            ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack) != 0) {
                float percent = DamageUtil.getPercent(stack);
                stacks.add(new DamageStack(stack, percent, i));
            }
        }

        stacks.sort(DamageStack::compareTo);
        if (stacks.size() <= 0) {
            int bestSlot = -1;
            MutableWrapper<Float> lowest = new MutableWrapper<>(Float.MAX_VALUE);
            MutableWrapper<ItemStack> bestStack = new MutableWrapper<>(ItemStack.EMPTY);
            for (SingleMendingSlot singleMendingSlot : module.singleMendingSlots) {
                if (!singleMendingSlot.isBlocked()) {
                    int slot = AutoArmour.iterateItems(!module.strict.getValue(), module.queuedSlots, stack -> {
                        if (AutoArmour.getSlot(stack) == singleMendingSlot.getSlot()) {
                            float percent = DamageUtil.getPercent(stack);
                            if (percent < lowest.get()) {
                                bestStack.set(stack);
                                lowest.set(percent);
                                return true;
                            }
                        }

                        return false;
                    });

                    bestSlot = slot == -1 ? bestSlot : slot;
                }
            }

            if (bestSlot != -1 && lowest.get() < 100.0f) {
                EntityEquipmentSlot equipmentSlot = AutoArmour.getSlot(bestStack.get());
                if (equipmentSlot != null) {
                    int slot = AutoArmour.fromEquipment(equipmentSlot);
                    if (bestSlot != -2) {
                        module.queueClick(bestSlot, bestStack.get(), dragIn, slot).setDoubleClick(module.doubleClick.getValue());
                    }

                    module.queueClick(slot, InventoryUtil.get(slot), bestStack.get());
                }
            }
            else if (!allBlocked)
            {
                module.unblockMendingSlots();
            }
        } else if (stacks.size() == 1) {
            DamageStack stack = stacks.get(0);
            SingleMendingSlot mendingSlot = Arrays.stream(module.singleMendingSlots)
                    .filter(s -> s.getSlot() == AutoArmour.getSlot(stack.getStack()))
                    .findFirst()
                    .orElse(null);
            if (mendingSlot != null && stack.getDamage() > ((Property<Integer>) module.damages[stack.getSlot() - 5]).getValue()) {
                MutableWrapper<ItemStack> drag = new MutableWrapper<>(dragIn);
                checkDamageStack(stack, mendBlock, drag);
                mendingSlot.setBlocked(true);
            }
        } else {
            MutableWrapper<ItemStack> drag = new MutableWrapper<>(dragIn);
            for (DamageStack stack : stacks) {
                if (checkDamageStack(stack, mendBlock, drag)) {
                    return;
                }
            }

            module.stage = MendingStage.MENDING;
        }
    }

    private boolean checkDamageStack(DamageStack stack, int mendBlock, MutableWrapper<ItemStack> drag) {
        ItemStack sStack = stack.getStack();
        int slot = AutoArmour.findItem(Items.AIR, !module.strict.getValue(), module.queuedSlots);
        if (slot == -1) {
            if (module.drag.getValue() && (module.stackSet || mc.player.inventory.getItemStack().isEmpty())) {
                if (checkMendingStage(mendBlock)) {
                    return true;
                }

                module.queueClick(stack.getSlot(), sStack, drag.get(), -1);
            }

            return true;
        } else if (slot != -2 && mc.player.inventory.getItemStack().isEmpty()) {
            if (checkMendingStage(mendBlock)) {
                return true;
            }

            module.queueClick(stack.getSlot(), sStack, drag.get(), slot).setDoubleClick(module.doubleClick.getValue());

            drag.set(sStack);
            ItemStack inSlot = InventoryUtil.get(slot);
            module.queueClick(slot, inSlot, drag.get());
            module.queuedSlots.add(slot);
            drag.set(inSlot);
        }

        return false;
    }

    private Map<EntityEquipmentSlot, Integer> setup(boolean xCarry) {
        boolean wearingBlast = false;
        Set<EntityEquipmentSlot> cursed = new HashSet<>(6);
        List<EntityEquipmentSlot> empty = new ArrayList<>(4);
        for (int i = 5; i < 9; i++) {
            ItemStack stack = InventoryUtil.get(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemArmor) {
                    int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);
                    if (lvl > 0) {
                        wearingBlast = true;
                    }
                } else {
                    empty.add(AutoArmour.fromSlot(i));
                }

                if (EnchantmentHelper.hasBindingCurse(stack)) {
                    cursed.add(AutoArmour.fromSlot(i));
                }
            } else {
                empty.add(AutoArmour.fromSlot(i));
            }
        }

        if (wearingBlast && empty.isEmpty()) {
            return new HashMap<>(1, 1.0f); // 2 for elytra
        }

        Map<EntityEquipmentSlot, LevelStack> map = new HashMap<>(6);
        Map<EntityEquipmentSlot, LevelStack> blast = new HashMap<>(6);

        for (int i = 8; i < 45; i++) {
            if (i == 5) {
                i = 9;
            }

            ItemStack stack = getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor) {
                float d = DamageUtil.getDamage(stack);
                ItemArmor armor = (ItemArmor) stack.getItem();
                EntityEquipmentSlot type = armor.getEquipmentSlot();
                int blastLvL = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);

                if (blastLvL != 0) {
                    compute(stack, blast, type, i, blastLvL, d);
                }

                int lvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);

                if (blastLvL != 0) {
                    if (lvl >= 4) {
                        lvl += blastLvL;
                    } else {
                        continue;
                    }
                }

                compute(stack, map, type, i, lvl, d);
            }

            if (i == 8 && xCarry) {
                i = 0;
            }
        }

        Map<EntityEquipmentSlot, Integer> result = new HashMap<>(6);
        if (wearingBlast) {
            for (EntityEquipmentSlot slot : empty) {
                if (map.get(slot) == null) {
                    LevelStack e = blast.get(slot);
                    if (e != null) {
                        map.put(slot, e);
                    }
                }
            }

            map.keySet().retainAll(empty);
            map.forEach((key, value) -> result.put(key, value.getSlot()));
        } else {
            boolean foundBlast = false;
            List<EntityEquipmentSlot> both = new ArrayList<>(4);
            for (EntityEquipmentSlot slot : empty) {
                LevelStack b = blast.get(slot);
                LevelStack p = map.get(slot);

                if (b == null && p != null) {
                    result.put(slot, p.getSlot());
                } else if (b != null && p == null) {
                    foundBlast = true;
                    result.put(slot, b.getSlot());
                } else if (b != null) {
                    both.add(slot);
                }
            }

            for (EntityEquipmentSlot b : both) {
                if (foundBlast) {
                    result.put(b, map.get(b).getSlot());
                } else {
                    foundBlast = true;
                    result.put(b, blast.get(b).getSlot());
                }
            }

            if (!foundBlast && !blast.isEmpty()) {
                Optional<Map.Entry<EntityEquipmentSlot, LevelStack>> first =
                        blast.entrySet()
                                .stream()
                                .filter(e -> !cursed.contains(e.getKey()))
                                .findFirst();

                first.ifPresent(e -> result.put(e.getKey(), e.getValue().getSlot()));
            }
        }

        return result;
    }

    private ItemStack getStack(int slot) {
        if (slot == 8) {
            return mc.player.inventory.getItemStack();
        }

        return InventoryUtil.get(slot);
    }

    private void compute(ItemStack stack, Map<EntityEquipmentSlot, LevelStack> map, EntityEquipmentSlot type, int slot, int level, float damage) {
        map.compute(type, (k, v) -> {
            if (v == null || !v.isBetter(damage, 35.0f, level, true)) {
                return new LevelStack(stack, damage, slot, level);
            }

            return v;
        });
    }


}
