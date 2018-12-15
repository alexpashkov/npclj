import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./index.css";
import Puzzle from "./Puzzle";

const ROOT_URL = "http://localhost:3000";
const getJSON = (url, options) => fetch(url, options).then(res => res.json());

class App extends Component {
  state = {
    puzzle: [[1, 2, 3], [8, 0, 4], [7, 6, 5]],
    size: 4
  };

  generate = () =>
    getJSON(ROOT_URL + "/random/" + this.state.size).then(({ result }) =>
      this.setState({ puzzle: result })
    );

  onSizeChange = ({ currentTarget: { value } }) =>
    this.setState({ size: +value });

  solve = () => {
    getJSON(ROOT_URL, {
      method: "POST",
      body: JSON.stringify({ puzzle: this.state.puzzle })
    }).then(data => {
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
