function [ value ] = func( m, mu, w, y, Z )
%Support vector classification function for HW 8
%   Returns the value of the function at the given point.


% calculate the penalty vector.
% penalty(i) is the penalty of the i'th component.
penalty = max(ones(m, 1) - y .* (Z * w) , zeros(m, 1));


% Calculat the value of the full function.
value = mu * norm(w) + 1/m * sum(penalty);

end

