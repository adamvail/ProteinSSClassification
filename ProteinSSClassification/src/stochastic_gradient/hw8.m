function hw8( )
%Main method of homework 8
%   Does all the experiments

data = load ('sg_homework.mat');

alpha_bar = 1.0e4;
mu = 1.0e-3;

final_val = stochastic_gradient( mu, alpha_bar, data.Z, data.y );

end

