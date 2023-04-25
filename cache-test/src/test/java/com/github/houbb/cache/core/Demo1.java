package com.github.houbb.cache.core;


public class Demo1 {
    class Father{
        int num = 11;
        Father(){
            System.out.println("父类构造器");
            show();

        }
        void show(){
            System.out.println("父类的show方法，并且num的值为"+num);
        }
    }
    class Son extends Father{
//        int num = 22;
        Son(int i){
            super();
            num = i;
            System.out.println("子类构造器");
            show();
        }
        void show(){
            System.out.println("子类的show方法，并且num的值为"+num);
            super.show();
        }
    }

    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.compareVersion("2.0","1.0");
    }
}

class Solution {
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int len1 = v1.length, len2 = v2.length;
        int i = 0;

        for(; i < Math.min(len1, len2); i++){
            int aVersion = compare(v1[i], v2[i]);
            if(aVersion != 0){
                return aVersion;
            }
        }

        while(i < len1){
            if(Integer.parseInt(v1[i]) != 0){
                return 1;
            }
            i++;
        }

        while(i < len2){
            if(Integer.parseInt(v2[i]) != 0){
                return -1;
            }
            i++;
        }

        return 0;
    }

    public int compare(String s1, String s2){
        int num1 = Integer.parseInt(s1);
        int num2 = Integer.parseInt(s2);

        if(num1 > num2) return 1;
        else if(num1 < num2) return -1;
        else return 0;
    }
}

