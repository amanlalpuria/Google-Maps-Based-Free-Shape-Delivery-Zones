# Google-Maps-Based-Free-Shape-Delivery-Zones
Google Maps-Based Free Shape Delivery Zones

**Description:**
Introduce an interactive delivery zone management feature that allows store owners to draw custom delivery areas (polygons) directly on a Google Map interface—similar to the Uber Eats "Free Shape Zone" capability.

The drawn zones will define delivery boundaries for each store. Each zone can have custom delivery fee logic (base fee, per-mile fee, minimum order amount) and will be stored in the database. These zones will also be used for:

* Finding nearby stores for users.
* Applying delivery fees when a delivery address falls within a zone or beyond a specified fee threshold (e.g., outside base coordinates).

**Objective:**

* Integrate Google Maps to allow store owners to draw polygonal shapes representing delivery areas.
* Store polygon coordinates and delivery zone metadata in the database.
* Apply delivery logic based on user address and the zones they fall into.
* Provide admin-level controls for configuring additional restrictions and delivery logic per zone.

---
![image](https://github.com/user-attachments/assets/df309e62-3575-4902-8a00-8f786f9d8e7b)

**Design Inspiration (Uber Eats Style Flow):**

* Step 1: Store Owner navigates to “Delivery Zones” settings.
* Step 2: Click on “Create Delivery Zone.”
* Step 3: Choose “Custom Shape” and draw polygon on Google Map.
* Step 4: Enter metadata such as:

  * Zone Name
  * Borough (if applicable)
  * Minimum Order Amount
  * Base Delivery Fee
  * Per Mile Fee (optional)
  * Estimated Delivery Time
  * Surge Multiplier
* Step 5: Optionally upload KML file for complex shapes.
* Step 6: Click "Save" to persist in the database.
* Step 7: Zones can be edited or deactivated later.

---

**Acceptance Criteria:**

* [ ] Google Maps UI allows store owners to draw and save polygons.
* [ ] Zones are stored with coordinates and metadata in a new `delivery_zones` table.
* [ ] Restrictions (e.g., delivery blackout times) can be added per zone via `delivery_restrictions` table.
* [ ] User app/backend should be able to:

  * Find which store zones contain a given user’s delivery address.
  * Calculate delivery fees based on whether address lies in base zone or extended area (using `per_mile_fee` and distance).
* [ ] Support viewing and editing of existing zones from store settings.
* [ ] Frontend should also support KML upload functionality (optional, MVP can skip).

---

**Database Schema (Confirmed):**

```sql
-- delivery_zones
CREATE TABLE delivery_zones (
    zone_id SERIAL PRIMARY KEY,
    store_id INTEGER NOT NULL,
    zone_name VARCHAR(255) NOT NULL,
    borough VARCHAR(50) NOT NULL,
    min_order_amount DECIMAL(10,2) NOT NULL,
    base_delivery_fee DECIMAL(10,2) NOT NULL,
    per_mile_fee DECIMAL(10,2),
    surge_multiplier DECIMAL(3,2) DEFAULT 1.0,
    estimated_delivery_time INTEGER, -- in minutes
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id)
);

-- delivery_restrictions
CREATE TABLE delivery_restrictions (
    id SERIAL PRIMARY KEY,
    zone_id INTEGER NOT NULL,
    restriction_type VARCHAR(50) NOT NULL,
    start_time TIME,
    end_time TIME,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (zone_id) REFERENCES delivery_zones(zone_id)
);
```

**To Be Added:**
A table to store polygon coordinates:

```sql
CREATE TABLE delivery_zone_coordinates (
    id SERIAL PRIMARY KEY,
    zone_id INTEGER NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    point_order INTEGER NOT NULL, -- to maintain polygon shape
    FOREIGN KEY (zone_id) REFERENCES delivery_zones(zone_id)
);
```

---

**Suggested Approach for UI/UX:**

* Use [[Google Maps JavaScript API – Drawing Library](https://developers.google.com/maps/documentation/javascript/drawinglayer)](https://developers.google.com/maps/documentation/javascript/drawinglayer)
* On polygon draw complete, extract lat/lngs and send along with delivery zone metadata to the backend
* Provide edit & delete support for polygons
* Optionally support uploading KML files for advanced users (can be deferred)

---

**Best Practice Note:**
Polygon drawing is preferred over typing in street names because:

* Streets do not define clean zones (many stores deliver to partial street segments).
* Polygons offer flexibility and accuracy, especially for large cities with irregular layouts.

---

**Dependencies:**

* Google Maps API key
* S3 already used for other uploads (optional for KML)
* Authenticated user context via `auditorAware`
* Frontend support for drawing tools

---

**Future Enhancements (Out of Scope for MVP):**

* Heatmaps for zone performance
* User-side store discovery via drawn delivery zones
* Time-based delivery fee variation per zone
