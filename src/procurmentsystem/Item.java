/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package procurmentsystem;

    public class Item {
        private String itemCode;
        private String itemName;
        private String itemDesc;
        private double pricePerUnit;
        private int moq;
        //supplier supplier
        private int recommendedSalePrice;


        public int getRecommendedSalePrice() {
            return recommendedSalePrice;
        }

        public void setRecommendedSalePrice(int recommendedSalePrice) {
            this.recommendedSalePrice = recommendedSalePrice;
        }

        public int getMoq() {
            return moq;
        }

        public void setMoq(int moq) {
            this.moq = moq;
        }

        public double getPricePerUnit() {
            return pricePerUnit;
        }

        public void setPricePerUnit(double pricePerUnit) {
            this.pricePerUnit = pricePerUnit;
        }

        public String getItemDesc() {
            return itemDesc;
        }

        public void setItemDesc(String itemDesc) {
            this.itemDesc = itemDesc;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }
    }
