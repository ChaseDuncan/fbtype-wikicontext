mvn compile
CP="./target/dependency/*:./target/classes/"

java -ea -Xmx90g -cp $CP edu.illinois.cs.cogcomp.fbtype.resources.ContextSentenceDB $1

