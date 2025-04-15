export interface Vehicle {
  id: number;
  imageUrl: string;           // Main image URL of the vehicle
  make: string;               // Make of the vehicle
  model: string;              // Model of the vehicle
  quantity: number;
  year: number;               // Year of manufacture
  color: string;              // Color of the vehicle
  vin: string;                // VIN (Vehicle Identification Number)
  mileage: number;            // Mileage of the vehicle
  seats: number;              // Number of seats
  retailPrice: number;        // Price per day for rental
  available: boolean;         // Availability of the vehicle
  location: string;           // Current location (GPS coordinates)
  insured: boolean;           // If the vehicle is insured
  vehicleCondition: string;          // Condition of the vehicle
  fuelLevel: number;          // Fuel level of the vehicle
  vehicleType: string;        // Type of vehicle (e.g., sedan, SUV, etc.)
  fuelType: string;           // Type of fuel used by the vehicle
  transmissionType: string;   // Type of transmission (manual/automatic)
  additionalPhotos?: string[]; // URLs of additional photos (optional)
}
