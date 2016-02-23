
package com.dsquare.hibour.pojos.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

        @SerializedName("FirstName")
        @Expose
        private String FirstName;
        @SerializedName("LastName")
        @Expose
        private String LastName;
        @SerializedName("Password")
        @Expose
        private String Password;
        @SerializedName("Image")
        @Expose
        private String Image;
        @SerializedName("Gender")
        @Expose
        private String Gender;
        @SerializedName("Email")
        @Expose
        private String Email;
        @SerializedName("Mobile Number")
        @Expose
        private String MobileNumber;
        @SerializedName("Address")
        @Expose
        private String Address;

        /**
         * @return The FirstName
         */
        public String getFirstName() {
            return FirstName;
        }

        /**
         * @param FirstName The FirstName
         */
        public void setFirstName(String FirstName) {
            this.FirstName = FirstName;
        }

        /**
         * @return The LastName
         */
        public String getLastName() {
            return LastName;
        }

        /**
         * @param LastName The LastName
         */
        public void setLastName(String LastName) {
            this.LastName = LastName;
        }

        /**
         * @return The Password
         */
        public String getPassword() {
            return Password;
        }

        /**
         * @param Password The Password
         */
        public void setPassword(String Password) {
            this.Password = Password;
        }

        /**
         * @return The Image
         */
        public String getImage() {
            return Image;
        }

        /**
         * @param Image The Image
         */
        public void setImage(String Image) {
            this.Image = Image;
        }

        /**
         * @return The Gender
         */
        public String getGender() {
            return Gender;
        }

        /**
         * @param Gender The Gender
         */
        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        /**
         * @return The Email
         */
        public String getEmail() {
            return Email;
        }

        /**
         * @param Email The Email
         */
        public void setEmail(String Email) {
            this.Email = Email;
        }

        /**
         * @return The MobileNumber
         */
        public String getMobileNumber() {
            return MobileNumber;
        }

        /**
         * @param MobileNumber The Mobile Number
         */
        public void setMobileNumber(String MobileNumber) {
            this.MobileNumber = MobileNumber;
        }

        /**
         * @return The Address
         */
        public String getAddress() {
            return Address;
        }

        /**
         * @param Address The Address
         */
        public void setAddress(String Address) {
            this.Address = Address;
        }

    }
