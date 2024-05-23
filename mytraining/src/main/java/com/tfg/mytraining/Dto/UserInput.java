/*
 * DTO para la entrada de datos del microservicio de predicción
 * Autor: Julio Navarro Vazquez
 * Versión: 1.0
 * Descripción: Esta clase representa la entrada de datos del usuario para el sistema de predicción.
  Incluye información sobre género, edad, peso, factor de actividad física (FAF),
  identificador de rutina y una valoración predeterminada.
 */

 package com.tfg.mytraining.Dto;

 import com.fasterxml.jackson.annotation.JsonProperty;
 
 
 public class UserInput {
 
     @JsonProperty("Gender")
     private int Gender;
     
     @JsonProperty("Age")
     private int Age;
     
     @JsonProperty("Weight")
     private double Weight;
     
     @JsonProperty("FAF")
     private double FAF;
     
     @JsonProperty("rutinaID")
     private String rutinaID;
     
     @JsonProperty("Valoracion")
     private double Valoracion = 2;
 
     public UserInput() {}
 
     public UserInput(int Gender, int Age, double Weight, double FAF, String rutinaID) {
         this.Gender = Gender;
         this.Age = Age;
         this.Weight = Weight;
         this.FAF = FAF;
         this.rutinaID = rutinaID;
     }
 
     public int getGender() { return Gender; }
     
     public void setGender(int Gender) { this.Gender = Gender; }
     
     public int getAge() { return Age; }
     
     public void setAge(int Age) { this.Age = Age; }
     
     public double getWeight() { return Weight; }
     
     public void setWeight(double Weight) { this.Weight = Weight; }
     
     public double getFAF() { return FAF; }
     
     public void setFAF(double FAF) { this.FAF = FAF; }
     
     public String getRutinaID() { return rutinaID; } 
     
     public void setRutinaID(String rutinaID) { this.rutinaID = rutinaID; } 
     
     public double getValoracion() { return Valoracion; }
 
     @Override
     public String toString() {
         return "UserInput{" +
                 "Gender=" + Gender +
                 ", Age=" + Age +
                 ", Weight=" + Weight +
                 ", FAF=" + FAF +
                 ", rutinaID='" + rutinaID + '\'' +
                 ", Valoracion=" + Valoracion +
                 '}';
     }
 }
 