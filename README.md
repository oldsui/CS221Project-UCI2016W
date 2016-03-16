# CS221Project-UCI2016W
A prototype search engine is built, limited to the subdomain of ics.uci.edu. 

![alt tag](https://raw.githubusercontent.com/oldsui/CS221Project-UCI2016W/master/screenshots/HomePage.PNG)



Pages crawled, indexing data and other large files are not included.

Ranking scheme:

Three features are used to compute the ranking, namely, tf-idf, page rank and title score (Jaccard Similarity between query and page title).

Interface:

Interface is built using JSP and servlet. Ready to deploy to a server.

Future work:

Store crawled pages and indexing in databases, such as MySQL, mongoDB, etc. 


Query Example (by cliking 'Go'):

![alt tag](https://raw.githubusercontent.com/oldsui/CS221Project-UCI2016W/master/screenshots/SearchResultsSnippets.PNG)


One click testing NDCG@5 (by cliking 'NDCG_TEST', ideal pages are results from Google):


![alt tag](https://raw.githubusercontent.com/oldsui/CS221Project-UCI2016W/master/screenshots/NDCG_Test_Result.PNG)

Team members:
Feng Hong
Rui Liu
Yanjun Liu


