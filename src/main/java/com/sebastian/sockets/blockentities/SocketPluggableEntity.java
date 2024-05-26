package com.sebastian.sockets.blockentities;

import com.sebastian.sockets.misc.SocketPlugable;
import com.sebastian.sockets.reg.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SocketPluggableEntity extends BlockEntity implements TickableBlockEntity, SocketPlugable {

    private final AdvancedEnergyStorage energy = new AdvancedEnergyStorage(2760, 230, 230, 0);
    private final LazyOptional<AdvancedEnergyStorage> energyOptional = LazyOptional.of(() -> this.energy);

    boolean pluggedIn;

    public SocketPluggableEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        if(nbt.contains("energy", Tag.TAG_INT)) {
            this.energy.deserializeNBT(nbt.get("energy"));
        }
        if(nbt.contains("plugged_in")) {
            pluggedIn = nbt.getBoolean("plugged_in");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("energy", this.energy.serializeNBT());
        pTag.putBoolean("plugged_in", pluggedIn);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.energyOptional.cast();
        } else {
            return super.getCapability(cap);
        }
    }

    public LazyOptional<AdvancedEnergyStorage> getEnergyOptional() {
        return this.energyOptional;
    }

    public AdvancedEnergyStorage getEnergy() {
        return this.energy;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void sendUpdate() {
        setChanged();

        if(this.level != null)
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyOptional.invalidate();
    }

    @Override
    public void tick() {
        sendUpdate();
    }

    @Override
    public Vec3 getConnectorPos(BlockState state) {
        return new Vec3(0.5,0.5,0.5);
    }

    @Override
    public int maxTransferCapacity() {
        return 20;
    }

    public void setPluggedIn(boolean pluggedIn) {
        this.pluggedIn = pluggedIn;
    }

    public boolean getPluggedIn() {
        return this.pluggedIn;
    }
}
