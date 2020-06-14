**Task-5**

***Additional links:***

Deploy link: http://EPBYMINW8147.minsk.epam.com:8080/news

Jenkins (Dev/developer): http://EPBYMINW8147.minsk.epam.com:8081/job/NewsProjectBuildAndDeployJob/

Sonarqube: http://EPBYMINW8147.minsk.epam.com:9000/dashboard?id=com.epam.lab%3Anews

***RestAPI***

**Author**
*	***GET:*** */authors - Gets all authors*;
*	***GET:*** */authors/id - Gets author by id*;
*	***POST:*** */authors - Persists author. Author should be sent using request-body in appropriate JSON-format*:

    {
        "name": "NewAuthorName",
        "surname": "NewAuthorSurname"
    }

*	***PUT:*** */authors - Merge author. Author should be sent using request-body in appropriate JSON-format*:

    {
	    "id": 11,
        "name": "AuthorName",
        "surname": "AuthorSurname"
    }

*	***DELETE:*** */authors/id - Delete author by id*.

**Tag**
*	***GET:*** */tags - Gets all tags*;
*	***GET:*** */tags/id - Gets tag by id*;
*	***POST:*** */tags - Persists tag. Tag should be sent using request-body in appropriate JSON-format*:

    {
        "name": "TagName"
    }

*	***PUT:*** */tags - Merge tag. Tag should be sent using request-body in appropriate JSON-format*:

    {
    	"id": 20,
        "name": "TagName"
    }

*	***DELETE:*** */tags/id - Delete tag by id*.

**News**
*	***GET:*** */news - Gets all news*;
*	***GET:*** */news/id - Gets news by id*;
*	***GET:*** */news?search=...&sort=... - Gets news according to search and sort conditions*. 
    

    Common search criteria format: criteria_name:value
    
        Available search criteria:
        - creationDate:yyyy-MM-dd - to find news by creation date;
        - modificationDate:yyyy-MM-dd - to find news by modification date;
        - author_name:AuthorName - to find news by author's name; 
        - author_surname:AuthorName - to find news by author's surname;
        - tags_name:TagName - to find news by tag's name.
    
    Common search criteria format: criteria_name:order
    
        Available sort criteria:
        - creationDate:yyyy-MM-dd - to find news by creation date;
        - modificationDate:yyyy-MM-dd - to find news by modification date;
        - author_name:AuthorName - to find news by author's name; 
        - author_surname:AuthorName - to find news by author's surname.
        
        Available order:
        - asc - ascending;
        - desc - descending.

*	***GET:*** */news/count - Gets news quantity*;
*	***POST:*** */news - Persists news. News should be sent using request-body in appropriate JSON-format*:

    {
        "title": "Title of this news",
        "shortText": "Short text of this news",
        "fullText": "Really long text of this news",
        "author": 
            {
                "id": 5,
                "name": "AuthorName",
                "surname": "AuthorSurname"
            },
        "tags": [
        	        {
        	        	"name": "the1TagName"
        	        },
        	        {
        		        "name": "the2TagName"
        	        }
                ]
    }

*	***PUT:*** */news - Merge news. News should be sent using request-body in appropriate JSON-format*:

    {
	    "id": 5,
        "title": "Title of this news",
        "shortText": "Short text of this news",
        "fullText": "Really long text of this news",
        "creationDate": "2020-10-10",
        "author": 
            {
		        "id": 4,
		        "name": "AuthorName",
		        "surname": "AuthorSurname"
		    },
        "tags": [
                    {
                        "id": 3,
                        "name": "TagThree"
                    },
                    {
                        "name": "NewTag"
                    }
                ]
    }

*	***DELETE:*** */news/id - Delete news by id*.