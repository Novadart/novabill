package com.novadart.novabill.shared.client.dto;

/**
 * Important note!
 * If you add/change payment types, remember to update the python code for invoice generation
 * @author Giordano Battilana
 *
 */
public enum PaymentType {
    CASH,
    
    BANK_TRANSFER,
    BANK_TRANSFER_30,
    BANK_TRANSFER_60,
    
    RIBA_30,
    RIBA_30_FM,
    RIBA_60,
    RIBA_60_FM,
    RIBA_90,
    RIBA_90_FM,
    RIBA_120,
    RIBA_120_FM,
    RIBA_150,
    RIBA_150_FM,
    RIBA_180,
    RIBA_180_FM
}
