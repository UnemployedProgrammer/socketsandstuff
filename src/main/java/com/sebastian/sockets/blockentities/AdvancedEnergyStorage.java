package com.sebastian.sockets.blockentities;

import net.minecraftforge.energy.EnergyStorage;

public class AdvancedEnergyStorage extends EnergyStorage {

    public AdvancedEnergyStorage(int capacity) {
        super(capacity);
    }

    public AdvancedEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public AdvancedEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public AdvancedEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergy(int energy) {
        if(energy < 0)
            energy = 0;
        if(energy > this.capacity)
            energy = this.capacity;

        this.energy = energy;
    }

    public void addEnergy(int energy) {
        setEnergy(this.energy + energy);
    }

    public void removeEnergy(int energy) {
        setEnergy(this.energy - energy);
    }
}
