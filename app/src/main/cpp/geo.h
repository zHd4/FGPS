#include <cstdlib>
#include <cmath>
#include <ctime>
#include <iostream>

class Geo {
private:
    constexpr static const double minLatitudeEurope = 30;
    constexpr static const double minLatitudeAmerica = 30;

    constexpr static const double maxLatitudeEurope = 70;
    constexpr static const double maxLatitudeAmerica = 60;

    constexpr static const double minLongitudeEurope = -10;
    constexpr static const double minLongitudeAmerica = -120;

    constexpr static const double maxLongitudeEurope = 160;
    constexpr static const double maxLongitudeAmerica = -75;

    bool inEurope() {
        return (short) random(0.0, 2.0);
    }

    double random(double min, double max) {
        srand(static_cast<unsigned int>(clock()));

        double randomDouble = double(rand()) / (double(RAND_MAX) + 1.0);
        return min + randomDouble * (max - min);
    }

    double roundToCoordinate(double value) {
        long roundCof = 1000000000;
        double result = round(value * roundCof) / roundCof;

        return result;
    }
public:
    double generateRandomLatitude() {
        bool latitudeEurope = inEurope();

        double minLatitude = latitudeEurope ? minLatitudeEurope: minLatitudeAmerica;
        double maxLatitude = latitudeEurope ? maxLatitudeEurope: maxLatitudeAmerica;

        return roundToCoordinate(random(minLatitude, maxLatitude));
    }

    double generateRandomLongitude() {
        bool longitudeEurope = inEurope();

        double minLongitude = longitudeEurope ? minLongitudeEurope : minLongitudeAmerica;
        double maxLongitude = longitudeEurope ? maxLongitudeEurope : maxLongitudeAmerica;

        return roundToCoordinate(random(minLongitude, maxLongitude));
    }
};