import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./index.css";
import Puzzle from "./Puzzle";

const ROOT_URL = "http://localhost:3000";
const getJSON = (url, options) => fetch(url, options).then(res => res.json());

class App extends Component {
  state = {
    solving: false,
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
    this.setState({ solving: true });
    getJSON(ROOT_URL, {
      method: "POST",
      body: JSON.stringify({ puzzle: this.state.puzzle })
    })
      .then(({ status, result }) => {
        if (status !== "ok") throw new Error(status);
        const showStates = states => {
          if (!states || !states.length) {
            return this.setState({ solving: false });
          }
          this.setState({
            puzzle: states[0]
          });
          setTimeout(showStates, 100, states.slice(1));
        };
        showStates(result.states);
      })
      .catch(err => {
        console.warn(err);
        this.setState({ solving: false });
      });
  };

  render() {
    const { puzzle, size, solving } = this.state;
    return (
      <div>
        <button onClick={this.generate} disabled={solving}>
          Generate
        </button>
        <input
          type="number"
          min="3"
          value={size}
          onChange={this.onSizeChange}
        />
        <Puzzle>{puzzle}</Puzzle>
        <button onClick={this.solve} disabled={solving}>
          Solve!
        </button>
      </div>
    );
  }
}

ReactDOM.render(<App />, document.getElementById("root"));
