# This is android application to show the routes information.

#######################################
# project follows MVVM architecture
#######################################
# Model classes are placed under model package, 
RoutesEntity is derived class by using API response.
RoutesEntity is also the Entity class for Room DB.

# View consists of below classes
SplashActivity for splash screen
MainActivity, which hosts the RoutesFragment.
RoutesFragment displays routes data.

# RoutesViewModel is the view model to hold the data in case screen configuration changes

#######################################
# Library usages
#######################################
Retrofit, Okhttp, and RxJava is used for networking
Room DB is used for structured data storage
