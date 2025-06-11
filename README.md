# Java-Based Financial Portfolio Rebalancer

A modern Java application that helps investors maintain their desired asset allocation by suggesting portfolio rebalancing actions based on predefined strategies.

## Features

### Core Functionality
- Portfolio management with multiple stocks
- Real-time portfolio value calculation
- Automatic allocation percentage calculation
- Multiple rebalancing strategies (currently implements Equal Weight Strategy)
- Modern, user-friendly GUI interface

### User Interface
- Clean, modern design using FlatLaf
- Input panel for adding new stocks
- Interactive portfolio table
- Real-time portfolio value display
- Rebalancing recommendations panel
- Styled buttons with hover effects
- Professional icons and visual elements

### Technical Features
- Built with Java 11
- Uses Maven for dependency management
- Implements Strategy Design Pattern for rebalancing strategies
- Uses Java Swing for GUI
- Includes SVG icons for better visual appeal
- Responsive and user-friendly interface

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
│   │   │           │   ├── Portfolio.java
│   │   │           │   └── Stock.java
│   │   │           ├── service/
│   │   │           │   └── PortfolioRebalancer.java
│   │   │           ├── strategy/
│   │   │           │   ├── RebalancingStrategy.java
│   │   │           │   └── EqualWeightStrategy.java
│   │   │           └── Main.java
│   │   └── resources/
│   │       └── icons/
│   │           ├── add.svg
│   │           ├── rebalance.svg
│   │           └── clear.svg
│   └── test/
└── pom.xml
```

## Dependencies

- **Jackson**: For JSON processing
- **Lombok**: For reducing boilerplate code
- **FlatLaf**: For modern UI look and feel
- **JUnit**: For testing (not implemented yet)

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

### Adding Stocks
1. Enter stock details in the input fields:
   - Symbol (e.g., AAPL)
   - Name (e.g., Apple Inc.)
   - Price (e.g., 150.00)
   - Quantity (e.g., 100)
2. Click "Add Stock" button
3. The stock will appear in the portfolio table

### Viewing Portfolio
- The portfolio table shows:
  - Stock symbol
  - Company name
  - Current price
  - Number of shares
  - Total value
  - Current allocation percentage
- Total portfolio value is displayed at the top

### Rebalancing Portfolio
1. Click "Rebalance Portfolio" button
2. View rebalancing recommendations in the bottom panel
3. Recommendations show:
   - Number of shares to buy
   - Number of shares to sell
   - Target allocations

### Clearing Portfolio
- Click "Clear Portfolio" to remove all stocks
- Confirmation dialog will appear

## Rebalancing Strategies

### Equal Weight Strategy
- Distributes portfolio value equally among all stocks
- Calculates target number of shares for each stock
- Suggests buy/sell actions to achieve equal allocation

## Future Enhancements

1. Additional Rebalancing Strategies:
   - Market Cap Weighted
   - Risk Parity
   - Custom Allocation

2. Data Features:
   - Real-time stock price updates
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