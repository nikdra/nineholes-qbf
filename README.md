# nineholes-qbf: A QBF generator for Nine Holes
nineholes-qbf is a Java program that generates QBF in non-cleansed QCIR format.
## Installation
To clone the repository and create an executable .jar file use the following commands:
```
git clone https://github.com/nikdra/nineholes-qbf
cd nineholes-qbf
javac -cp ./src/main ./src/main/*.java -d ./out/
jar cvfm nineholes-qbf.jar ./src/main/resources/META-INF/MANIFEST.MF -C ./out/ .
```
## Usage
The application requires three parameters: the number of steps (must be greater 0 and odd), the mode (classic or nested) and a file which contains the initial position of the counters on the board. The first line of the file represents the white counters and the second line the black counters. A '0' means that there is no counter of that kind on the board, a '1' means there is one. Here is an example:
```
110100000
000010011
```
Note that the program does not check if this is a valid board state, e.g. that there are not two counters in the same position.
The classic mode produces a formula similar to the one described in Ansoteguì et al. (2005). The nested mode is based on an idea suggested by Tomáš Peitl during my work on my bachelor thesis.  
```
java -jar nineholes-qbf.jar [steps] [mode] [FILE]
```
