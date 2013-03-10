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
    BANK_TRANSFER_90,
    BANK_TRANSFER_120,
    BANK_TRANSFER_150,
    BANK_TRANSFER_180,
    
    RIBA_30,
    RIBA_60,
    RIBA_90
}
