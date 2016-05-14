# CS221Project-UCI2016W
A search engine prototype is built, limited to the domain of ics.uci.edu. 




Pages crawled, indexing data and other large files are not included.

Ranking scheme:

Ranking Score = f(tf-idf, page rank, title score)
Three features are used to compute the ranking, namely, tf-idf, page rank and title score (Jaccard Similarity between query and page title).

Interface:

Interface is built using JSP and servlet. Ready to deploy to a server.

Future work:

Store crawled pages and indexing in databases, such as MySQL, mongoDB, etc. 


Demonstration on Chrome (screenshots):

HomePage: 'Go' - normal search; 'NDCG_TEST' - compute NDCG@5 given 10 queries to adjust the formula for ranking score

![alt tag](https://raw.githubusercontent.com/oldsui/CS221Project-UCI2016W/master/screenshots/HomePage.PNG)


Query Example (by cliking 'Go'):

![alt tag](https://raw.githubusercontent.com/oldsui/CS221Project-UCI2016W/master/screenshots/SearchResultsSnippets.PNG)



Team members:

Feng Hong, Rui Liu, Yanjun Liu


