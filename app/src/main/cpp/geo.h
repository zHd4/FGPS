#include <cstdlib>
#include <cmath>
#include <ctime>

class Geo {
private:
    constexpr static const double minLatitude = -90;
    constexpr static const double maxLatitude = 90;

    constexpr static const double minLongitude = -180;
    constexpr static const double maxLongitude = 80;

    double random(double min, double max) {
        srand(static_cast<unsigned int>(clock()));

        double randomDouble = double(rand()) / (double(RAND_MAX) + 1.0);
        return min + randomDouble * (max - min);
    }

    double roundToCoordinate(double value) {
        long roundCof = 1000000000;
        double result = floor(value * roundCof) / roundCof;

        result = value < 0 ? -result : result;

        return result;
    }
public:
    double generateRandomLatitude() {
        return roundToCoordinate(random(minLatitude, maxLatitude));
        //return random(minLatitude, maxLatitude);
    }

    double generateRandomLongitude() {
        return roundToCoordinate(random(minLongitude, maxLongitude));
    }
};
