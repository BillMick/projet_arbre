package org.example.Models;

public class Vote {
        private String description;

        public Vote(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }

}
