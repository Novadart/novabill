package com.novadart.novabill.shared.client.util;

import com.novadart.novabill.shared.client.dto.AccountingDocumentItemDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AccountingCalcUtils {

    private static final BigDecimal BD_100 = BigDecimal.valueOf(100);

    public static Map<BigDecimal, List<AccountingDocumentItemDTO>> partitionItemsByTax(List<AccountingDocumentItemDTO> items){
        Map<BigDecimal, List<AccountingDocumentItemDTO>> partitions = new TreeMap<BigDecimal, List<AccountingDocumentItemDTO>>();

        BigDecimal tmpTax;
        List<AccountingDocumentItemDTO> tmpItems;

        for (AccountingDocumentItemDTO a : items) {
            if(a.getPrice() == null){
                continue;
            }

            tmpTax = a.getTax();
            tmpItems = partitions.get(tmpTax);

            if(tmpItems == null){
                tmpItems = new ArrayList<AccountingDocumentItemDTO>();
                partitions.put(tmpTax, tmpItems);
            }

            tmpItems.add(a);
        }

        return partitions;
    }

    public static AccountingDocumentTotals calculatePartialTotals(BigDecimal tax, List<AccountingDocumentItemDTO> accountingDocumentItems){
        BigDecimal totalBeforeTaxes = BigDecimal.ZERO;

        for (AccountingDocumentItemDTO a : accountingDocumentItems) {
            totalBeforeTaxes = totalBeforeTaxes.add(a.getTotalBeforeTax());
        }

        //	calc total before taxes
        BigDecimal roundedTotalBeforeTaxes = round2Dec(totalBeforeTaxes);

        //	calc total taxes
        BigDecimal taxesPercent = tax.divide(AccountingCalcUtils.BD_100); // for example 22 / 100 = 0.22
        BigDecimal totalTaxes = roundedTotalBeforeTaxes.multiply(taxesPercent);
        BigDecimal roundedTaxes = round2Dec(totalTaxes);

        // calc total after taxes
        BigDecimal roundedTotalAfterTaxes = roundedTotalBeforeTaxes.add(roundedTaxes); // already rounded

        AccountingDocumentTotals result = new AccountingDocumentTotals();
        result.setTotalBeforeTaxes(roundedTotalBeforeTaxes);
        result.setTotalTaxes(roundedTaxes);
        result.setTotalAfterTaxes(roundedTotalAfterTaxes);
        return result;
    }

    public static AccountingDocumentTotals calculateTotals(List<AccountingDocumentItemDTO> accountingDocumentItems){

        Map<BigDecimal, List<AccountingDocumentItemDTO>> partitions = partitionItemsByTax(accountingDocumentItems);

        BigDecimal totalBeforeTaxes = BigDecimal.ZERO;
        BigDecimal totalTaxes = BigDecimal.ZERO;

        AccountingDocumentTotals partialTotals;
        for (BigDecimal tax : partitions.keySet()) {
            partialTotals = calculatePartialTotals(tax, partitions.get(tax));
            totalBeforeTaxes = totalBeforeTaxes.add(partialTotals.getTotalBeforeTaxes());
            totalTaxes = totalTaxes.add(partialTotals.getTotalTaxes());
        }

        BigDecimal totalAfterTaxes = totalBeforeTaxes.add(totalTaxes);

        // all values are sum of rounded values, thus no rounding is required
        AccountingDocumentTotals result = new AccountingDocumentTotals();
        result.setTotalBeforeTaxes(totalBeforeTaxes);
        result.setTotalTaxes(totalTaxes);
        result.setTotalAfterTaxes(totalAfterTaxes);
        return result;
    }

    public static BigDecimal round2Dec(BigDecimal value){
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateTotalBeforeTaxesForItem(AccountingDocumentItemDTO item){
        if(item.getPrice() == null){
            return BigDecimal.ZERO;
        }

        BigDecimal discount = item.getDiscount()==null ?
                BigDecimal.ONE :
                    item.getDiscount().multiply(BigDecimal.valueOf(-1)).add(BD_100).divide(BD_100);
        BigDecimal total = item.getPrice()
                .multiply(item.getQuantity())
                .multiply(discount);

        // rounded because this way document total will be precise
        return round2Dec(total);
    }

    public static BigDecimal calculateTotalWeight(AccountingDocumentItemDTO item){
        if(item.getWeight() == null){
            return BigDecimal.ZERO;
        }

        return item.getWeight().multiply(item.getQuantity());
    }

    public static BigDecimal calculateTotalWeight(List<AccountingDocumentItemDTO> items){
        BigDecimal total = BigDecimal.ZERO;
        for (AccountingDocumentItemDTO i : items) {
            total = total.add(calculateTotalWeight(i));
        }
        return total;
    }

    public static class AccountingDocumentTotals {
        private BigDecimal totalBeforeTaxes;
        private BigDecimal totalTaxes;
        private BigDecimal totalAfterTaxes;

        public BigDecimal getTotalBeforeTaxes() {
            return totalBeforeTaxes;
        }
        public void setTotalBeforeTaxes(BigDecimal totalBeforeTaxes) {
            this.totalBeforeTaxes = totalBeforeTaxes;
        }
        public BigDecimal getTotalTaxes() {
            return totalTaxes;
        }
        public void setTotalTaxes(BigDecimal totalTaxes) {
            this.totalTaxes = totalTaxes;
        }
        public BigDecimal getTotalAfterTaxes() {
            return totalAfterTaxes;
        }
        public void setTotalAfterTaxes(BigDecimal totalAfterTaxes) {
            this.totalAfterTaxes = totalAfterTaxes;
        }
    }
}
