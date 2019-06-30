// You can print the values to stdout for debugging

public class Solution{  
    
    
    public static int[] sortArray( int arr[] ){  
        int i , max , location , j , temp , len = arr.length;
        for( i = 0 ; i < len ; i ++ ){
           max = arr[i];
           location = i;
           for( j = i ; j < len ; j ++ ){
             if( max > arr[j] ){
                max = arr[j];
                location = j;
             }
           }
           temp = arr[i];
           arr[i] = arr[location];
           arr[location] = temp;
        }   
        return arr;
      }  

      public static void main(String[] args){
        int arr[]=new int[]{1,2,3,4};
        int out[]=new int[4];
        out=sortArray(arr);
        for( j = i ; j < len ; j ++ ){
            if( max > arr[j] ){
               max = arr[j];
               location = j;
            }
        System.out.println(out);
    }
      }




