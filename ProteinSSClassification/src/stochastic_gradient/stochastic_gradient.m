function [ func_value ] = stochastic_gradient( mu, alpha_bar, Z, y )
%Classical stochastic gradient function
%   This function implements the classical stochastic gradient algorithm,
%   using a weighting parameter of mu, and a minimum step size of
%   alpha_bar.  Returns the value of the function at the final w.

[m, n] = size(Z);

% Choose initial w at random from the standard normal distribution
w_next = randn(n, 1);

val = zeros(m + 1, 1); % Save value of function at each value of k
%classed = zeros(m + 1, 1); % Save # of y classified correctly for each value of k.


% Iterate m times
for k = 1 : m
    
    val(k, 1) = func(m, mu, w_next, y, Z);
    %classed(k, 1) = count_classed_correctly(w_next,y,Z);
    
    w = w_next;
    
    % Set i_k to an integer between 1 and m inclusive, chosen uniformly at
    % random.
    i_k = randi(m);
    
    % Calculate the step size for this iteration
    alpha_k = min([alpha_bar; 1/(k * mu)]);
    
    % Calculate the subgradient for the sample i_k
    g_i_k = subgrad(mu, w, y(i_k), Z(i_k, :).');
    
    % Take a step in the negative g_i_k direction
    w_next = w - alpha_k * g_i_k;

end

val(m+1, 1) = func(m, mu, w_next, y, Z);
%classed(m+1, 1) = count_classed_correctly(w_next,y,Z);

subplot(2, 1, 1), plot (val)
%subplot(2, 1, 2), plot (classed)

% plot how many w meet metric, too.

func_value = func(m, mu, w_next, y, Z);

end

