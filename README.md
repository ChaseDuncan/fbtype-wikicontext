# Freebase/WikiContext

This module types Wikipedia pages using Freebase types. 
It provides course types, fine types or CoNLL types, (person, location, organization),
depending on the users needs. Some code examples follow.

```java
import edu.illinois.cs.cogcomp.fbtype.FreebaseType;

public static void main(String args[]){
        FreebaseType freebaseType = new FreebaseType();
        String curIdPer = "15162268";  //person.author person
        String curIdNull = "38608933"; //written_work
        String curIdLoc = "29551290";  //location geography.glacier
    
        String titlePer = "Richard_Irving_Dodge";  //person.author person
        String titleNull = "The_Seven_Wonders_(Saylor_novel)"; //written_work
        String titleLoc = "Mondor_Glacier";  //location geography.glacier
        
        // access types by curid
        System.out.println(freebaseType.getCourseTypesById(curIdPer));
        System.out.println(freebaseType.getCourseTypesById(curIdNull));
        System.out.println(freebaseType.getCourseTypesById(curIdLoc));
        
        // output:
        // $[person]
        // $[written_work]
        // $[geography, location
        
        // or access by Wikipedia title
        System.out.println(freebaseType.getCourseTypesById(titlePer));
        System.out.println(freebaseType.getCourseTypesById(titleNull));
        System.out.println(freebaseType.getCourseTypesById(titleLoc));
        
        // output:
        // $[person]
        // $[written_work]
        // $[geography, location
}

```

Additionally, this module provides the context sentences from Wikipedia in which a given
title appears along with the surface form of the referent page in the context.

Example:

TODO: add an example
