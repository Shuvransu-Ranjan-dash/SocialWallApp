# SocialWallApp
Social Wall App Flow Diagram
1️⃣ User actions:
•	User visits Home Page (home.html)
•	User can:
o	Create a post (with optional image) → Form submission to /posts
o	Like a post → POST to /posts/{id}/like
o	View comments → GET /posts/{id}/comments
o	Add comment → POST /posts/{id}/comments/add
o	Edit post → GET /posts/{id}/edit → POST /posts/{id}/edit
o	Delete post → POST /posts/{id}/delete
2️⃣ Backend flow:
•	Controller (PageController) handles requests
•	Post data stored in Post table
•	Comment data stored in Comments table
•	Image uploaded stored in /uploads/ directory
•	After operations, user redirected to home page (home.html)
3️⃣ Data flow:
User -> Browser -> Controller (PageController) -> Service/Repository -> Database
      <- Controller <- Repository <- Database <- Response <- Browser
________________________________________
I can also convert this into a proper diagram image (like PNG) or a PDF flowchart using standard flowchart symbols:
Example Layout for the Diagram:
[Home Page]
     |
     |-- Create Post --> [PostController.savePost] --> [Post Repository] --> [Database: posts table]
     |
     |-- View Posts --> [PageController.home] --> [Post Repository] --> [Database: posts table] --> display on Home Page
     |
     |-- Like Post --> [PostController.likePost] --> [Post Repository] --> [Database]
     |
     |-- View Comments --> [PageController.comments] --> [Comment Repository] --> [Database: comments table]
     |                                |
     |                                -- Add Comment --> [Comment Repository] --> [Database]
     |
     |-- Edit Post --> [PageController.editPost / updatePost] --> [Post Repository] --> [Database]
     |
     |-- Delete Post --> [PageController.deletePost] --> [Post Repository + Comment Repository] --> [Database]
 
