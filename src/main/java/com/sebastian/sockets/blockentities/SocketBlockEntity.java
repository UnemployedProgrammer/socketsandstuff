package com.sebastian.sockets.blockentities;

import com.sebastian.sockets.misc.ConnectionState;
import com.sebastian.sockets.reg.AllBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SocketBlockEntity extends BlockEntity implements TickableBlockEntity {

    int deviceX = 0;
    int deviceY = 0;
    int deviceZ = 0;
    boolean connected = false;

    public SocketBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(AllBlockEntities.SOCKET_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private final AdvancedEnergyStorage energy = new AdvancedEnergyStorage(2760, 230, 230, 0);
    private final LazyOptional<AdvancedEnergyStorage> energyOptional = LazyOptional.of(() -> this.energy);

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        if(nbt.contains("energy", Tag.TAG_INT)) {
            this.energy.deserializeNBT(nbt.get("energy"));
        }
        if(nbt.contains("device")) {
            CompoundTag connectedDevice = nbt.getCompound("device");
            if(connectedDevice.contains("x")) deviceX = connectedDevice.getInt("x");
            if(connectedDevice.contains("y")) deviceY = connectedDevice.getInt("y");
            if(connectedDevice.contains("z")) deviceZ = connectedDevice.getInt("z");
            if(connectedDevice.contains("is_connected")) connected = connectedDevice.getBoolean("is_connected");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("energy", this.energy.serializeNBT());
        CompoundTag connectedDevice = new CompoundTag();
        connectedDevice.putInt("x", deviceX);
        connectedDevice.putInt("y", deviceY);
        connectedDevice.putInt("z", deviceZ);
        connectedDevice.putBoolean("is_connected", connected);
        pTag.put("device", connectedDevice);
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
        tryMovingEnergy();
        sendUpdate();
    }

    public ConnectionState setConnected(int x, int y, int z) {
        if(level != null && !level.isClientSide()) {
            if(connected) return ConnectionState.ALREADY_CONNECTED_SOCKET;
            if(level.getBlockEntity(new BlockPos(x, y, z)) instanceof SocketPluggableEntity entity) {
                if(entity.getPluggedIn()) return ConnectionState.ALREADY_CONNECTED_DEVICE;
                connected = true;
                entity.setPluggedIn(true);
                deviceX = x;
                deviceY = y;
                deviceZ = z;
                return ConnectionState.SUCCESS;
            }
            return ConnectionState.BE_NOT_FOUND;
        }
        return ConnectionState.CONNECTION_ERROR;
    }

    public ConnectionState breakConnection() {
        if(!connected) return ConnectionState.NOT_CONNECTED;
        if(level != null && !level.isClientSide()) {
            if(level.getBlockEntity(new BlockPos(deviceX, deviceY, deviceZ)) instanceof SocketPluggableEntity entity) {
                if(!entity.getPluggedIn()) return ConnectionState.NOT_CONNECTED;
                connected = false;
                entity.setPluggedIn(false);
                deviceX = 0;
                deviceY = 0;
                deviceZ = 0;
                return ConnectionState.BROKEN;
            }
            return ConnectionState.BE_NOT_FOUND;
        }
        return ConnectionState.COULD_NOT_BREAK;
    }

    public void tryMovingEnergy() {
        //use recieveEnergy(...,false)
        if(connected) {
            if(level != null && !level.isClientSide()) {
                if(level.getBlockEntity(new BlockPos(deviceX, deviceY, deviceZ)) instanceof SocketPluggableEntity entity) {
                    if(this.getEnergy().getEnergyStored() >= entity.maxTransferCapacity()) {
                        int moved = entity.getEnergy().receiveEnergy(entity.maxTransferCapacity(), false);
                        this.getEnergy().removeEnergy(moved);
                    }
                } else {
                    connected = false;
                    deviceX = 0;
                    deviceY = 0;
                    deviceZ = 0;
                }
            }
        }
    }
}
