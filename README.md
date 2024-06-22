# To-Do App
This is a simple To-Do application built using Kotlin, Jetpack libraries, MVVM architecture, and Room database. for codsoft internship

![32967e53-efc7-48c0-b065-ced0aa08d794](https://github.com/m-akrm/todo-app/assets/113924991/6295412b-a144-4430-90fb-4d534c309d82)
![efa6356c-924e-45e4-9b10-fc6b19b3fe0c](https://github.com/m-akrm/todo-app/assets/113924991/7449060c-f1dd-4e09-ac38-824d084423df)
![2e78b841-e68b-4b01-94ff-6c3e1e798a6c](https://github.com/m-akrm/todo-app/assets/113924991/7b0c2763-a4e1-4933-8a90-a2cde4abcd1b)


## Features
- **Task Management**: Add, edit, and delete tasks.
- **Calendar Integration**: Schedule tasks with a single-row calendar view.
- **Kotlin-Based**: Developed using Kotlin for robust performance.
- **persistence**: used room database for data persistence
  
## Technologies Used
- Kotlin: Programming language
- Jetpack Components:
  - ViewModel: For managing UI-related data in a lifecycle-conscious way
  - LiveData: For observable data holder class
  - DataBinding: To bind UI components in layouts to data sources
- Room: For local database management
  
## Architecture
The app follows the Model-View-ViewModel (MVVM) architecture pattern:

- Model: Manages the data layer using Room database.
- View: Displays the data and forwards user interactions to the ViewModel.
- ViewModel: Acts as a communication center between the Repository (data layer) and the View.
  
## Code Overview
### Dataclasses
- task: Define the task entity and its related project name
- project: define the project , number of tasks and completed percentage
### Databases
the getall*() methods are integrated with livedata which make thedatabse the single source of truth 
### ViewModel
an activity view model is used in order to share the  data between fragements

## License
This project is licensed under the MIT License - see the LICENSE file for details.
