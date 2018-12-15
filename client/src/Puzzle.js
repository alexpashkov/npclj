import React from 'react';

const Tile = ({val}) => <div className="tile">{!!val && val}</div>

const Row = ({tiles}) => <div className="row">{tiles.map(val => <Tile key={val} val={val}/>)}</div>;

const Puzzle = ({children}) => <div className="puzzle">{children.map(row => <Row key={row.join()} tiles={row}/>)}</div>;

export default Puzzle;
