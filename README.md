# Java Advanced Techniques

# Lab 1
An application that allows you to check the indicated directories for changes in the files they contain.

# Lab 2
An application that allows you to view the content of files (.png and .txt) while browsing the directory structure. 

The app was designed with weak references. When viewing a list of files in a given directory, the contents of the current file are loaded into a HashMap. Weak reference allows you to bypass the need to load the same content multiple times - which can happen when moving forward and backward through the list of files in a given directory.

The application indicates whether the contents of the file have been reloaded or taken from memory.
# Lab 3

An application that allows you to consume data obtained from a website offering a public rest API "https://api.teleport.org/api/countries/.

The application uses fetched data for testing user geography knowledge, by asking the question "Which country has a higher population?" and collects results.

# Lab 4
An application that allows you to outsource tasks to dynamically loaded class instances of custom class loader. This task was accomplished using the reflection API.

# Lab 5
An application with a graphical interface (Swing) that allows you to perform cluster analysis on tabular data.

# Lab 6
An application that simulates network of billboards, on wich advertisments are displayed. Clients can send request for displaying thair ads to the manager and if any billboard has free slot it will be displayed for specifide in the order span of time. The manager, as well as the client, can withdraw the order earlier.

The application uses RMI to connect instances of the applications.

