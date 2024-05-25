package com.sebastian.sockets.blockentities;

import com.sebastian.sockets.reg.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SocketBlockEntity extends BlockEntity implements TickableBlockEntity {
    public SocketBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AllBlockEntities.SOCKET_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private final EnergyStorage energy = new EnergyStorage(200, 100, 100, 0);
    private final LazyOptional<EnergyStorage> energyOptional = LazyOptional.of(() -> this.energy);

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        if(nbt.contains("energy", Tag.TAG_INT)) {
            this.energy.deserializeNBT(nbt.get("energy"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("energy", this.energy.serializeNBT());
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ENERGY) {
            return this.energyOptional.cast();
        } else {
            return super.getCapability(cap);
        }
    }

    public LazyOptional<EnergyStorage> getEnergyOptional() {
        return this.energyOptional;
    }

    public EnergyStorage getEnergy() {
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
}
