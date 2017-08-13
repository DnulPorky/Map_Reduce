 	[MultiThreads Project]

 	++Finding expressions using MapReduce paradigm++

 	This Java project purpose is to build a multithread software that search for multiple expressions in multiple files/folders.
 	The search and process of its partial results are distributed using threads. In order to do this parallelism of tasks 
I used the ReplicatedWorkers model and the MapReduce paradigm.
 	The software receives a list of expressions and a list of files that are to be searched, along with the number of threads 
that will be created.It will split all the files that are to be searched in subparts with specified size(a fixed number of 
lines set by user, except the last parts that may be smaller). Every subpart will be searched for every expression and 
will return a partial result, how many times has found every expression and at which lines. 
	Than the partial results will be assembled for every file and in the end we'll have the results for every expression, 
how many times it was found, which files contained it and at which line.The final results will be written in the specified 
output file.
    
	The project compiling arguments are : number of threads that should be created , input file path and output file path.

 	Input file has the following structure:
Line 1: - number of lines(NL) contained by the subparts;
Line 2: - number of expressions(NE) to be searched;
Next NE lines: - the NE expressions, one expression per line;
Line 3 + NE: - number of text files(NF) to be searched for expressions;
Next NF lines: - the files, one per line.

	Output file will show following informations for every expressions: total number of appearances, the files and 
the lines of those appearances.
	Output file has following structure:
Expression 1: [number of appearances]
[name of file]: [line number][line number][line number]...
[name of file]: [line number][line number][line number]...

	First of all the user will specify as arguments how many threads will be created and the path to input and output files.
Than the software will extract from input file the size of future subparts(nr of lines), the list of expressions and the list 
of files to be searched.Every file will be split in specified size subparts and every subpart will be searched for expressions
(this is the Map part of the Map Reduce model), and the results of those searches will be assembled for final results(Reduce 
part of MapReduce model).
	In order to created the parallelism of multithreading ReplicatedWorkers model we create a list of tasks. Every task is 
composed of a file subpart  which must be searched for expressions and create partial results: how manny times has found 
every expression and at which lines of the subpart(practically a hashmap with expressions as keys and integer arrays for 
lines nr, first element of the array is the number of exppression encounters).Then a workpool of workers will be created 
and every worker will ask for a task and process it until there are no more tasks and only then the thread is terminated.
The number of workers is independent for the number of tasks and every worker does both map(searching files subparts for 
expressions) and reduce jobs(puts partial results in a structure to be further process).
In the end the second part of reduce job is pretty straight forward,  partial results are processed and assembled in one 
final structure, an array of objects that contains an expression and the lines nr where it is found, for every file. 
 