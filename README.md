# Freebase Type/WikiContext

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

Additionally, this module provides the context sentences in which a given title appears in Wikipedia along
with its referent surface form within that context.

Example:

```java
public static void main(String args[]){
    
    WikiContext wikiContext = new WikiContext();
    
    // access context by curid
    String curId1 = "534366";
    
    ArrayList<Pair<String, String>> context1 = wikiContext.getContextByCurId(curId1);
    System.out.println("SURFACE: " + context1.get(0).getFirst());
    System.out.println("CONTEXT: " + context1.get(0).getSecond());
    
    // output:
    // SURFACE: Barack Obama
    // CONTEXT: In the 2008 presidential election , Democrat Barack Obama improved on Kerry 's showing , 
    // and took 88.7 % of the vote in the Bronx to Republican John McCain 's 10.9 % .
    
    
    // access context by title
    String curId1 = "534366";
    String title1 = "Barack_Obama";
    context1 = wikiContext.getContextByTitle(title1);
    System.out.println("SURFACE: " + context1.get(0).getFirst());
    System.out.println("CONTEXT: " + context1.get(0).getSecond());
    
    // output:
    // SURFACE: Barack Obama
    // CONTEXT: In the 2008 presidential election , Democrat Barack Obama improved on Kerry 's showing , 
    // and took 88.7 % of the vote in the Bronx to Republican John McCain 's 10.9 % .
}

```
