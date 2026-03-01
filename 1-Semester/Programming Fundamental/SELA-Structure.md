
Main tumhare SELA project ko **pure components/modules** mein break karke ek clean architecture de raha hoon.

Ye breakdown tumhara code **scalable, readable aur viva-friendly** banayega.

---

# ✅ **SELA PROJECT — COMPLETE MODULAR BREAKDOWN (PROFESSIONAL STRUCTURE)**

## 📌 **TOP-LEVEL PROJECT STRUCTURE**

```
/SELA
│
├── src/
│   ├── main.c
│   ├── graphics.c
│   ├── buildingInfo.c
│   ├── appliances.c
│   ├── loadCalculator.c
│   ├── parking.c
│   ├── lifts.c
│   └── utils.c
│
├── include/
│   ├── graphics.h
│   ├── buildingInfo.h
│   ├── appliances.h
│   ├── loadCalculator.h
│   ├── parking.h
│   ├── lifts.h
│   └── utils.h
│
└── data/
    └── applianceData.h   (static wattage database)
```

---

# ⭐ **COMPONENT 1 — graphics.c / graphics.h**

**Purpose:**
Start screen, building drawing, menu.

### graphics.h

```c
void initGraphics();
void drawBuilding();
int mainMenu();
void closeGraphics();
```

### graphics.c

```c
void initGraphics() { /* graphics.h initwindow() etc */ }
void drawBuilding() { /* rectangles, windows */ }
int mainMenu() { /* print menu, return user choice */ }
void closeGraphics() { /* closegraph(); */ }
```

---

# ⭐ **COMPONENT 2 — buildingInfo.c / buildingInfo.h**

**Purpose:**
Building related information: floors, homes, parking.

### buildingInfo.h

```c
typedef struct {
    int floors;
    int homesPerFloor;
    int totalHomes;
    int hasParking;
} Building;

Building inputBuildingInfo();
```

### buildingInfo.c

```c
Building inputBuildingInfo() {
    Building b;
    printf("Enter number of floors: ");
    scanf("%d", &b.floors);

    printf("Enter number of homes per floor: ");
    scanf("%d", &b.homesPerFloor);

    b.totalHomes = b.floors * b.homesPerFloor;

    printf("Parking area? (1=Yes,0=No): ");
    scanf("%d", &b.hasParking);

    return b;
}
```

---

# ⭐ **COMPONENT 3 — applianceData.h (STATIC DATABASE)**

**Purpose:**
All company wattage values ek jagah save.

### applianceData.h

```c
// FAN
#define ORIENT_FAN     75
#define DAWLANCE_FAN   80
#define HAIER_FAN      70
#define PHILIPS_FAN    65

// LED LIGHT
#define PHILIPS_LED    12
#define OSRAM_LED      14
#define SURYA_LED      12

// REFRIGERATOR
#define HAIER_FRIDGE   250
#define DAWLANCE_FRIDGE 200
#define ORIENT_FRIDGE  220

// AC
#define GREE_AC        1800
#define HAIER_AC       1600
#define DAWLANCE_AC    1700

// TV
#define SAMSUNG_TV     120
#define SONY_TV        100
#define TCL_TV         90

// WM
#define HAIER_WM       500
#define DAWLANCE_WM    450
#define SAMSUNG_WM     650
```

---

# ⭐ **COMPONENT 4 — appliances.c / appliances.h**

**Purpose:**
User se home appliances ka data lena + individual load calculate karna.

### appliances.h

```c
typedef struct {
    float fanLoad;
    float lightLoad;
    float fridgeLoad;
    float acLoad;
    float tvLoad;
    float tubeLoad;
    float wmLoad;
    float totalLoad;
} Home;

Home inputHomeAppliances(int homeNumber);
int selectBrand(char* applianceName, char* companies[], int wattages[], int options);
```

### appliances.c

```c
Home inputHomeAppliances(int homeNumber) {
    Home h = {0};
    int count, choice;

    // FAN
    printf("Home %d: Number of fans? ", homeNumber);
    scanf("%d", &count);
    if(count > 0) {
        char* fanCompanies[] = {"Orient","Dawlance","Haier","Philips"};
        int fanWatts[] = {ORIENT_FAN, DAWLANCE_FAN, HAIER_FAN, PHILIPS_FAN};
        choice = selectBrand("Fan", fanCompanies, fanWatts, 4);
        h.fanLoad = count * fanWatts[choice];
    }

    // Repeat same pattern for LED, Fridge, AC, TV, Tube, Washing Machine…

    h.totalLoad = h.fanLoad + h.lightLoad + h.fridgeLoad 
                + h.acLoad + h.tvLoad + h.tubeLoad + h.wmLoad;

    return h;
}
```

---

# ⭐ **COMPONENT 5 — loadCalculator.c / loadCalculator.h**

**Purpose:**
Total building load, floor load, home load processing.

### loadCalculator.h

```c
float calculateTotalLoad(Home homes[], int totalHomes);
void printHomeLoads(Home homes[], int totalHomes, int homesPerFloor);
void printFloorLoads(Home homes[], int floors, int homesPerFloor);
```

### loadCalculator.c

```c
float calculateTotalLoad(Home homes[], int totalHomes) {
    float sum = 0;
    for(int i=0;i<totalHomes;i++)
        sum += homes[i].totalLoad;
    return sum;
}
```

---

# ⭐ **COMPONENT 6 — parking.c / parking.h**

### parking.h

```c
float parkingLoad();
```

### parking.c

```c
float parkingLoad() {
    return (10 * 40) + (2 * 100) + (4 * 36); 
    // Example: 10 LEDs + 2 security lights + 4 tube lights
}
```

---

# ⭐ **COMPONENT 7 — lifts.c / lifts.h**

### lifts.h

```c
float liftLoad();
```

### lifts.c

```c
float liftLoad() {
    return 2 * 2500; // two lifts
}
```

---

# ⭐ **COMPONENT 8 — utils.c / utils.h**

Helper functions.

### utils.h

```c
void clearScreen();
void waitForKey();
```

---

# ⭐ **COMPONENT 9 — main.c (FINAL CLEAN MAIN FILE)**

Ye file sab ko integrate karegi.

### main.c (structure)

```c
#include <stdio.h>
#include "graphics.h"
#include "buildingInfo.h"
#include "appliances.h"
#include "loadCalculator.h"
#include "parking.h"
#include "lifts.h"

int main() {
    initGraphics();
    drawBuilding();

    int choice = mainMenu();
    if(choice != 1) return 0;

    Building b = inputBuildingInfo();

    Home homes[b.totalHomes];

    for(int i=0;i<b.totalHomes;i++) {
        homes[i] = inputHomeAppliances(i+1);
    }

    float buildingLoad = calculateTotalLoad(homes, b.totalHomes);

    if(b.hasParking)
        buildingLoad += parkingLoad();

    buildingLoad += liftLoad();

    // OUTPUT
    printHomeLoads(homes, b.totalHomes, b.homesPerFloor);
    printFloorLoads(homes, b.floors, b.homesPerFloor);

    printf("\nTotal Building Load: %.2f Watts\n", buildingLoad);

    closeGraphics();
    return 0;
}
```

---
