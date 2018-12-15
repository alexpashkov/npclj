import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./index.css";
import Puzzle from "./Puzzle";

class App extends Component {
  state = {
    puzzle: [[1, 2, 3], [8, 0, 4], [7, 6, 5]],
    size: 4
  };

  generate = () => {
    this.setState({
      puzzle: generate(this.state.size)
    });
  };

  onSizeChange = ({ currentTarget: { value } }) =>
    this.setState({ size: +value });

  solve = () => {
    fetch("http://localhost:3000", {
      method: "POST",
      body: JSON.stringify({ puzzle: this.state.puzzle })
    })
      .then(d => d.text())
      .then(data => {
        debugger;
      });
  };

  render() {
    const { puzzle, size } = this.state;
    return (
      <div>
        <button onClick={this.generate}>Generate</button>
        <input
          type="number"
          min="3"
          value={size}
          onChange={this.onSizeChange}
        />
        <Puzzle>{puzzle}</Puzzle>
        <button onClick={this.solve}>Solve!</button>
      </div>
    );
  }
}

ReactDOM.render(<App />, document.getElementById("root"));
