// import './../js/jquery-3.7.1.min.js';

async function loadTextFromFile(fileName){
    console.debug('Inside loadTextFromFile');
    const response = await fetch(fileName);
    const txtContent = await response.text();
    return txtContent;
}

String.prototype.toRouteSegment = function() {
    return this.replace(/\s/g,'').toLowerCase();
}

async function loadCategories() {
    console.debug('Inside loadCategories');
    let text = await loadTextFromFile('/data.json');
    console.debug("Text is: " + text);
    let jsonObj = JSON.parse(text);
    let categories = jsonObj.categories;
    for (let i=0; i < categories.length; i++){  
        $('.grid').append(`
            <div class="g-col-4">
            <div class"card">
                <div class="card-body">
                <h5>${categories[i].name}</h5>
                <img src="${categories[i].cover}" alt="${categories[i].name}" class="card-image-top img-fluid"/>
                <a href='/categories/${categories[i].name.toRouteSegment()}' class="btn btn-primary">${categories[i].name}</a>
                </div>
            </div>
            </div>
        `);
        
        // $('.grid-container').html(`<div class="grid-item">${category}</div>`);
    }
}

async function loadCourses() {
    let result = await loadTextFromFile('/data.json');
    console.debug('Response is: ' + result);
    let jsonObj = JSON.parse(result);
    let courses = jsonObj.categories[1].items;
    for (var i = 0; i < courses.length; i++){
        let course = courses[i];
        console.debug('Book: ' + course.name);
        $('.grid').append(`
        <div class="g-col-6">
           <div class="card">
             <div class="card-body">
                <img src="${course.cover}" alt="${course.name}" class="card-image-top img-fluid">
                <h5>${course.name}</h5>
                <img src="${course.instructorImage}" alt="${course.instructor} round-circle" object-fit="cover" class="img-fluid profile">
                <h6>${course.instructor}</h6>
                <a href="/categories/courses/${course.name.toRouteSegment()}" class="btn btn-primary">Course Details</a>
             </div>
           </div> 
        </div>
        `);
    }
    
}

async function loadBooks() {
    let result = await loadTextFromFile('/data.json');
    console.debug('Response is: ' + result);
    let jsonObj = JSON.parse(result);
    let books = jsonObj.categories[0].items;
    for (var i = 0; i < books.length; i++){
        let book = books[i];
        console.debug('Book: ' + books.name);
        $('.grid').append(`
        <div class="g-col-4">
           <div class="card">
             <div class="card-body">
                <img src="${book.cover}" alt="${book.name}" class="card-image-top img-fluid">
                <h5>${book.name}</h5>
                <img src="${book.authorImage}" alt="${book.author} round-circle" object-fit="cover" class="img-fluid profile">
                <h6>${book.author}</h6>
                <a href="/categories/books/${book.name.toRouteSegment()}" class="btn btn-primary">Book Details</a>
             </div>
           </div> 
        </div>
        `);
    }
    
}

/**
 * Load books from JSON file
 * @returns The list of books
 */
async function getBooksFromDb(){
    let rawText = await loadTextFromFile('/data.json');
    let jsonObj = JSON.parse(rawText);
    let books = jsonObj.categories[0].items;
    return books;
}

async function loadBookDetails(bookName){
    let books = await getBooksFromDb();

    console.debug(`Loaded books: ${books.length}`);
    let book = books.find((book)=> {
        return book.name.toRouteSegment() == bookName
    });

    console.debug(`Found book is: ${book.name}`);
    const layoutBook = (book) => {
        $('.book-details').append(`
            <div class="card">
             <img src="${book.cover}" alt="${book.name}" class="card-top-image">
             <div class="card-body">
              <h5 class="card-title">${book.name}</h5>
              <p class="card-text">
              ${book.description}
              </p>
              <div class="card-body">
               <label for="author">Author:</label>
               <em for="author">${book.author}</em>
               <img src="${book.authorImage}" class="profile">
              </div>
             </div>
            </div>
        `);
    };
    layoutBook(book);
}

/**
 * Loads courses from our database
 * @returns the list of courses
 */
async function getCoursesFromDb() {
    let rawText = await loadTextFromFile('/data.json');
    let jsonObj = JSON.parse(rawText);
    let courses = jsonObj.categories[1].items;
    return courses;    
}
/**
 * Loads course details from our JSON data for the given course name
 * @param {string} courseName
 */
async function loadCourseDetails(courseName){
    let courses = await getCoursesFromDb(courseName);

    console.debug(`Loaded courses: ${courses.length}`);
    let course = courses.find((course)=> {
        return course.name.toRouteSegment() == courseName
    });

    console.debug(`Found course is: ${course.name}`);
    const layoutCourse = (course) => {
        $('.course-details').append(`
            <div class="card">
             <img src="${course.cover}" alt="${course.name}" class="card-top-image">
             <div class="card-body">
              <h5 class="card-title">${course.name}</h5>
              <p class="card-text">
              ${course.description}
              </p>
              <div class="card-body">
               <label for="instructor">Instructor:</label>
               <em id="instructor">${course.instructor}</em>
               <img src="${course.instructorImage}" class="profile">
              </div>
             </div>
            </div>
        `);
    };
    layoutCourse(course);    
}

/**
 * Searches the given segment in course titles and updates the document
 * title using a matching name
 * @param {string} courseSegment 
 */
async function updateTitleFromCourseSegment(courseSegment){
    let courses = await getCoursesFromDb();
    let course = courses.find((course)=>{
      return course.name.toRouteSegment() == courseSegment
    }
    );
    //Update the document title if a match is found
    if (course){
      document.title = course.name;
    }
}

/**
 * Searches the given segment in book titles and updates the document
 * title using a matching name
 * @param {string} bookSegment 
 */
async function updateTitleFromBookSegment(bookSegment){
    let books = await getBooksFromDb();
    let book = books.find((book) => {
        return book.name.toRouteSegment() == bookSegment
    });
    //Update the document title if a match was found
    if(book){
        document.title = book.name;
    }
}