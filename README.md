# Java-Based Financial Portfolio Rebalancer

A modern Java application that helps investors maintain their desired asset allocation by suggesting portfolio rebalancing actions based on predefined strategies.
![Screenshot](images/screenshot.png)
## Features

### Core Functionality
- Portfolio management with multiple asset types (stocks, bonds, ETFs, etc.)
- Real-time portfolio value calculation
- Automatic allocation percentage calculation
- Multiple rebalancing strategies (Threshold-Based and Equal Weight)
- Modern, user-friendly GUI interface
- Comprehensive unit tests for core logic

### User Interface
- Clean, modern design using FlatLaf
- Input panel for adding new assets
- Interactive portfolio table
- Real-time portfolio value display
- Rebalancing recommendations dialog
- Styled buttons and professional icons

### Technical Features
- Built with Java 11
- Uses Maven for dependency management
- Implements Strategy Design Pattern for rebalancing strategies
- Uses Java Swing for GUI
- Includes SVG icons for better visual appeal
- Responsive and user-friendly interface
- Unit tests with JUnit

## Project Structure

```
portfolio-rebalancer/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── portfolio/
│   │   │           ├── gui/
│   │   │           │   └── PortfolioGUI.java
│   │   │           ├── model/
│   │   │           │   ├── Asset.java
│   │   │           │   ├── AssetType.java
│   │   │           │   └── Portfolio.java
│   │   │           ├── service/
│   │   │           │   └── PortfolioRebalancer.java
│   │   │           ├── strategy/
│   │   │           │   ├── RebalancingStrategy.java
│   │   │           │   ├── ThresholdRebalancingStrategy.java
│   │   │           │   └── EqualWeightStrategy.java
│   │   │           └── Main.java
│   │   └── resources/
│   │       └── icons/
│   │           ├── rebalance.svg
│   └── test/
│       └── java/
│           └── com/
│               └── portfolio/
│                   ├── model/
│                   │   ├── AssetTest.java
│                   │   └── PortfolioTest.java
│                   └── strategy/
│                       └── ThresholdRebalancingStrategyTest.java
└── pom.xml
```

## Dependencies

- **Jackson**: For JSON processing
- **Lombok**: For reducing boilerplate code
- **FlatLaf**: For modern UI look and feel
- **JUnit**: For testing

## Setup Instructions

### Prerequisites
- Java 11 or later
- Maven 3.6 or later

### Installation
1. Clone the repository
2. Navigate to the project directory
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn exec:java
   ```

## Usage Guide

### Adding Assets
1. Click "Add Asset" and enter asset details:
   - Symbol (e.g., AAPL)
   - Name (e.g., Apple Inc.)
   - Type (Stock, Bond, ETF, etc.)
   - Quantity (e.g., 10)
   - Price (e.g., 150.00)
   - Target Allocation (e.g., 0.2 for 20%)
2. Click "Add" to add the asset to your portfolio

### Viewing Portfolio
- The portfolio table shows:
  - Symbol
  - Name
  - Type
  - Quantity
  - Price
  - Value
  - Target %
  - Current %
  - Deviation from target
- Total portfolio value is displayed at the top

### Rebalancing Portfolio
1. Select a rebalancing strategy (Threshold-Based or Equal Weight)
2. Adjust the threshold if needed
3. Click "Rebalance"
4. View rebalancing recommendations in the dialog
   - Shows buy/sell amounts for each asset

### Removing Assets
- Select an asset in the table and click "Remove Selected"

## Rebalancing Strategies

### Threshold-Based Rebalancing
- Suggests trades for assets whose allocation deviates from the target by more than the specified threshold

### Equal Weight Strategy
- Distributes portfolio value equally among all assets
- Suggests buy/sell actions to achieve equal allocation

## Testing

- Run all unit tests with:
  ```bash
  mvn test
  ```
- Tests cover asset calculations, portfolio logic, and rebalancing strategies

## Future Enhancements

1. Additional Rebalancing Strategies:
   - Market Cap Weighted
   - Risk Parity
   - Custom Allocation

2. Data Features:
   - Real-time price updates
   - Historical performance tracking
   - Transaction history

3. UI Improvements:
   - Dark mode
   - Custom themes
   - Portfolio visualization charts

4. Additional Features:
   - Save/load portfolios
   - Export reports
   - Multiple portfolio management
   - Tax-loss harvesting suggestions

## Contributing

Feel free to contribute to this project by:
1. Forking the repository
2. Creating a feature branch
3. Submitting a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- FlatLaf for the modern UI components
- SVG icons from Material Design Icons
- Java Swing for the GUI framework 
