# Freebase Type/Wikipedia Context

The Freebase Type/Wikipedia Context module provides entity types and sentence level context
for Wikipedia entities in any language. The typing is course, fine or CoNLL types, 
(person, location, organization, miscellaneous), depending on the users needs. 

Some code examples follow.

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

The context module gives the context sentences in which a given title appears in Wikipedia (in any language) along
offsets of the surface form of the title in that context.

Example:

```java
public static void main(String args[]){
    
    WikiContext wikiContext = new WikiContext("config/test.config");
    ArrayList<Pair<Pair<Integer, Integer>, String>> context1 = null;
    
    String title1 = "\"Weird_Al\"_Yankovic";
    
    context1 = wikiContext.getContextByTitle(title1);
    for(Object context : context1)
        System.out.println(context);
                
    // output:
    //((0, 17), Weird Al Yankovic made a song parody of the virus titled "Virus Alert".)
    //((0, 19), "Weird Al" Yankovic's "Angry White Boy Polka" medley included Limp Bizkit's song "My Way".)
    //((5, 22), When Weird Al Yankovic was asked whether "Mad" had had any influence in putting him on a road 
    // to a career in parody, the musician replied, "[It was] more like going off a cliff.")
    // ...
}

```

We can also retrieve context by curid:

```java
public static void main(String args[]){
    
    WikiContext wikiContext = new WikiContext("config/test.config");
    ArrayList<Pair<Pair<Integer, Integer>, String>> context1 = null;
    
    String curId1 = "534366";
    
    context1 = wikiContext.getContextByTitle(curId1);
    for(Object context : context1)
        System.out.println(context);
                
    
    // output:
    //((0, 12), Barack Obama was born and raised in Hawaii (other than a four-year period of his childhood spent in 
    // Indonesia) and made Illinois his home and base after completing law school and later represented the state in 
    // the US Senate.)
    //((0, 15), President Obama, Secretary of Defense Leon Panetta, and Admiral Mike Mullen, Chairman of the Joint 
    // Chiefs of Staff, sent the certification required by the Repeal Act to Congress on July 22, 2011, setting the 
    // end of DADT for September 20, 2011.)
    //((6, 18), After Barack Obama won the general election, Jello wrote an open letter making suggestions on how to 
    // run his term as president.)
    //((6, 18), After Barack Obama, Powell was only the second African American to receive electoral votes in a 
    // presidential election.)
    // ...
}

```

The MultilingualFreebaseType and MultilingualWikiContext classes provide the ability to retrieve type and context
for any Wikipedia page in any language so long as it has a corresponding English page. Queries can either be in the
target language of English. Here of some examples of this in Spanish:

```java
    String config = "config/es-test.config";
    MultilingualWikiContext multilingualWikiContext = new MultilingualWikiContext(config);

    MultilingualFreebaseType freebaseType = new MultilingualFreebaseType(config);
    
    ArrayList<Pair<Pair<Integer, Integer>, String>> esContext =
            multilingualWikiContext.getContextByENTitle("Hydrochloric_acid");
    
    System.out.println(esContext.get(2));
    // Output:
    // ((29, 46),"Es atacado lentamente por el ácido clorhídrico (HCl) en presencia de aire.")
    
    
    System.out.println(freebaseType.getFineTypesByTitle("Ácido_clorhídrico"));
    // Output:
    // [chemistry]
```

All of this functionality is supported by a number of resources which must be built individually based on what
the user needs. The module fbtype.resources holds a number of classes which build and manage these resources.
For each resource a script and configuration file is provided.
